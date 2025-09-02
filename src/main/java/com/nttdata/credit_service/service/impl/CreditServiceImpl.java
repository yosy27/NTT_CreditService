package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.client.customer.CustomerClient;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.model.CreditMovementDocument;
import com.nttdata.credit_service.repository.CreditMovementRepository;
import com.nttdata.credit_service.repository.CreditRepository;
import com.nttdata.credit_service.service.CreditService;
import com.nttdata.credit_service.service.mapper.CreditMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.nttdata.credit_service.util.Validators.requireNonNegativeMono;
import static com.nttdata.credit_service.util.Validators.requirePositiveMono;
import static java.time.Instant.now;
import static org.springframework.util.StringUtils.hasText;


// Implementación del servicio de créditos con programación reactiva
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepo;
    private final CreditMovementRepository movementRepo;
    private final CustomerClient customerClient;

    @Override
    public Mono<Credit> registerCredit(CreditCreate req) {
        if (req == null || req.getType() == null || req.getLimit() == null || req.getInterestAnnual() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "type, limit, interestAnnual are required"));
        }
        if (req.getLimit().compareTo(BigDecimal.ZERO) < 0 || req.getInterestAnnual().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit and interestAnnual must be >= 0"));
        }
        // 1) Obtener customerId de forma robusta
        Mono<String> customerIdMono;
        if (hasText(req.getCustomerId())) {
            customerIdMono = Mono.just(req.getCustomerId().trim());
        } else if (req.getDocumentType() != null && hasText(req.getDocumentNumber())) {
            // si tu enum generado tiene getValue(): usa req.getDocumentType().getValue()
            String docType = req.getDocumentType().getValue();  // o req.getDocumentType().name() según tu modelo
            String docNum  = req.getDocumentNumber().trim();
            customerIdMono = customerClient.resolveCustomerId(docType, docNum)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")));
        } else {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide customerId OR documentType+documentNumber"));
        }

        // 2) Reglas y guardado
        return customerIdMono
                .flatMap(cid -> ensureBusinessRulesOnCreate(cid, req.getType())
                        .then(Mono.defer(() -> creditRepo.save(CreditMapper.fromCreate(req, cid)))))
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<Credit> getCredit(String id) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<Credit> patchCredit(String id, CreditUpdate patch) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    CreditMapper.merge(doc, patch);
                    return creditRepo.save(doc);
                })
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<OverdueStatus> getDebtStatusByCustomer(String customerId) {
        return customerClient.existsCustomerId(customerId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND"));
                    }
                    return creditRepo.findByCustomerId(customerId)
                            .collectList()
                            .map(list -> {
                                BigDecimal total = list.stream()
                                        .filter(c -> "DELINQUENT".equalsIgnoreCase(c.getStatus()))
                                        .map(c -> c.getBalance() == null ? BigDecimal.ZERO : c.getBalance())
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                OverdueStatus dto = new OverdueStatus();
                                dto.setHasOverdue(total.compareTo(BigDecimal.ZERO) > 0);
                                dto.setTotalOverdueAmount(total);
                                return dto;
                            });
                });
    }

    @Override
    public Mono<Credit> adjustLimit(String id, BigDecimal newLimit, String reason) {

        return requireNonNegativeMono(newLimit, "newLimit")
                .then(creditRepo.findById(id))
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                    .flatMap(doc -> {
                        var balance = nvl(doc.getBalance(), BigDecimal.ZERO);
                        if (newLimit.compareTo(balance) < 0) {
                            // Regla: el nuevo límite no puede ser menor al saldo usado
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "New limit < current balance"));
                        }
                        doc.setLimit(newLimit);
                        doc.setUpdatedAt(now());
                        return creditRepo.save(doc);
                    })
                    .map(CreditMapper::toApi);
    }

    @Override
    public Mono<Credit> closeCredit(String id, String reason) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var bal = nvl(doc.getBalance(), BigDecimal.ZERO);
                    if (bal.compareTo(BigDecimal.ZERO) > 0) {
                        // Regla: solo se cierra con balance 0
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CREDIT_INVALID_STATE"));
                    }
                    if (enumName(CreditStatus.CLOSED).equals(doc.getStatus())) {
                        return Mono.just(doc); // idempotente
                    }
                    doc.setStatus(enumName(CreditStatus.CLOSED));
                    doc.setClosedAt(now());
                    doc.setCloseReason(hasText(reason) ? reason.trim() : null);
                    doc.setUpdatedAt(now());
                    return creditRepo.save(doc);
                })
                .map(CreditMapper::toApi);
    }

    @Override
    public Flux<CreditMovement> listMovements(String creditId, String type) {
        if (hasText(type)) {
            return movementRepo.findByCreditIdAndTypeOrderByTxnAtDesc(creditId, type.trim())
                    .map(CreditMapper::toApi);
        }
        return movementRepo.findByCreditIdOrderByTxnAtDesc(creditId)
                .map(CreditMapper::toApi);
    }

    @Override
    public Flux<Credit> listCredits(String customerId, CreditType type, CreditStatus status, Boolean includeClosed) {
        final boolean incClosed = Boolean.TRUE.equals(includeClosed);

        // Arranque del stream
        Flux<CreditDocument> stream =
                (hasText(customerId))
                        ? creditRepo.findByCustomerId(customerId.trim())
                        : creditRepo.findAll();

        // Filtros
        if (type != null) {
            final String typeStr = enumName(type);
            stream = stream.filter(d -> typeStr.equals(d.getType()));
        }
        if (status != null) {
            final String statusStr = enumName(status);
            stream = stream.filter(d -> statusStr.equals(d.getStatus()));
        } else if (!incClosed) {
            stream = stream.filter(d -> !enumName(CreditStatus.CLOSED).equals(d.getStatus()));

        }

        return stream.map(CreditMapper::toApi);
    }

    @Override
    public Mono<CreditBalance> getBalance(String id) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .map(CreditMapper::toApiBalance);
    }

    @Override
    public Mono<CreditMovement> applyPayment(String id, CreditPaymentRequest req) {
        return  requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(creditRepo.findById(id))
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                    .flatMap(doc -> {
                        var balance = nvl(doc.getBalance(), BigDecimal.ZERO);
                        if (req.getAmount().compareTo(balance) > 0) {
                            // Si no permites pagos mayores al saldo
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Payment exceeds balance"));
                        }
                        var newBal = balance.subtract(req.getAmount());
                        doc.setBalance(newBal);
                        doc.setUpdatedAt(java.time.Instant.now());
                        return creditRepo.save(doc)
                                .flatMap(saved -> {
                                    var m = new CreditMovementDocument();
                                    m.setCreditId(saved.getId());
                                    m.setType("PAYMENT");
                                    m.setAmount(req.getAmount());
                                    var now = java.time.Instant.now();
                                    m.setTxnAt(now);
                                    m.setPostedAt(now);
                                    m.setRunningBalance(newBal);
                                    m.setChannel(req.getChannel() != null ? req.getChannel().getValue() : null);
                                    // m.setMerchant(null); // los pagos no llevan merchant
                                    return movementRepo.save(m).map(CreditMapper::toApi);
                                });
                    });
    }

    @Override
    public Mono<CreditMovement> applyCharge(String id, CreditChargeRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(creditRepo.findById(id)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                    .flatMap(doc -> {
                        var limit = nvl(doc.getLimit(), BigDecimal.ZERO);
                        var balance = nvl(doc.getBalance(), BigDecimal.ZERO);
                        var available = limit.subtract(balance);
                        boolean isCard = enumName(CreditType.CREDIT_CARD).equals(doc.getType());

                        if (isCard && req.getAmount().compareTo(available) > 0) {
                            return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CREDIT_CARD_LIMIT_EXCEEDED"));
                        }

                        var newBal = balance.add(req.getAmount());
                        // Para PERSONAL/BUSINESS: si no quieres permitir exceder límite, valida:
                        if (!isCard && newBal.compareTo(limit) > 0) {
                            return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "New balance exceeds limit"));
                        }

                        doc.setBalance(newBal);
                        doc.setUpdatedAt(java.time.Instant.now());
                        return creditRepo.save(doc)
                                .flatMap(saved -> {
                                    var m = new CreditMovementDocument();
                                    m.setCreditId(saved.getId());
                                    m.setType("CHARGE");
                                    m.setAmount(req.getAmount());
                                    var now = java.time.Instant.now();
                                    m.setTxnAt(now);
                                    m.setPostedAt(now);
                                    m.setRunningBalance(newBal);
                                    m.setChannel(req.getChannel() != null ? req.getChannel() : "CARD");
                                    return movementRepo.save(m).map(CreditMapper::toApi);
                                });
                    }));
    }

    @Override
    public Flux<Credit> searchByDocument(String documentType, String documentNumber) {
        return customerClient.resolveCustomerId(documentType, documentNumber.trim())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
                .flatMapMany(cid -> creditRepo.findByCustomerId(cid))
                .map(CreditMapper::toApi);
    }


    private Mono<Void> ensureBusinessRulesOnCreate(String customerId, CreditType type) {
        if (type == CreditType.PERSONAL) {
            return creditRepo.existsByCustomerIdAndTypeAndStatus(customerId, enumName(CreditType.PERSONAL), enumName(CreditStatus.ACTIVE))
                    .flatMap(exists -> exists
                            ? Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CREDIT_PERSONAL_LIMIT"))
                            : Mono.empty());
        }
        return Mono.empty();
    }

    private static BigDecimal nvl(BigDecimal v, BigDecimal def) { return v == null ? def : v; }

    private static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        try { return Enum.valueOf(type, value); } catch (Exception e) { return null; }
    }

    private static String enumName(Enum<?> e) { return e == null ? null : e.name(); }
}
