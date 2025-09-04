package com.nttdata.credit_service.service.impl;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.repository.CreditMovementRepository;
import com.nttdata.credit_service.repository.CreditRepository;
import com.nttdata.credit_service.service.CreditService;
import com.nttdata.credit_service.service.domain.CreditLedger;
import com.nttdata.credit_service.service.mapper.CreditMapper;
import com.nttdata.credit_service.service.policy.CreditCreationPolicy;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.nttdata.credit_service.model.util.CreditUtils.*;
import static com.nttdata.credit_service.model.util.Validators.requireNonNegativeMono;
import static com.nttdata.credit_service.model.util.Validators.requirePositiveMono;
import static java.time.Instant.now;
import static org.springframework.util.StringUtils.hasText;


// Implementación del servicio de créditos con programación reactiva
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepo;
    private final CreditMovementRepository movementRepo;
    private final CustomerClient customerClient;
    private final CreditCreationPolicy creditCreationPolicy;
    private final CreditLedger creditLedger;


    @Override
    public Mono<CreditResponse> registerCredit(CreditRequest req) {
        if (req == null || req.getType() == null || req.getLimit() == null || req.getInterestAnnual() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "type, limit, interestAnnual are required"));
        }
        if (req.getLimit().compareTo(BigDecimal.ZERO) < 0 || req.getInterestAnnual().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit and interestAnnual must be >= 0"));
        }

        // No permitir enviar ambos
        if (hasText(req.getCustomerId()) && req.getDocumentType() != null && hasText(req.getDocumentNumber())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide only customerId OR documentType+documentNumber, not both"));
        }

        Mono<String> customerIdMono;

        if (hasText(req.getCustomerId())) {
            String cid = req.getCustomerId().trim();
            customerIdMono = customerClient.existsCustomerId(cid)
                    .flatMap(exists -> exists
                            ? Mono.just(cid)
                            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")));
        } else if (req.getDocumentType() != null && hasText(req.getDocumentNumber())) {
            String docType = req.getDocumentType().getValue(); // o .name()
            String docNum  = req.getDocumentNumber().trim();
            customerIdMono = customerClient.resolveCustomerId(docType, docNum)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")));
        } else {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Provide customerId OR documentType+documentNumber"));
        }

        return customerIdMono
                .flatMap(cid ->  creditCreationPolicy.validate(cid, req.getType())
                        .then(Mono.defer(() -> creditRepo.save(CreditMapper.fromCreate(req, cid)))))
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<CreditResponse> getCredit(String id) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<CreditResponse> patchCredit(String id, CreditUpdate patch) {
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
    public Mono<CreditResponse> adjustLimit(String id, BigDecimal newLimit, String reason) {

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
    public Mono<CreditResponse> closeCredit(String id, String reason) {
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
    public Flux<CreditResponse> listCredits(String customerId, CreditType type, CreditStatus status, Boolean includeClosed) {
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
    public Mono<CreditResponse> updateCredit(String id, CreditRequest req) {
        return creditRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    // aplica SOLO los campos editables (la lógica vive en tu mapper)
                    CreditMapper.mergeAll(doc, req);
                    doc.setUpdatedAt(now());
                    return creditRepo.save(doc);
                })
                .map(CreditMapper::toApi);
    }

    @Override
    public Mono<Void> deleteCredit(String id) {
        return creditRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.empty();
                    // limpia movimientos (si no tienes deleteByCreditId, hacemos deleteAll del stream)
                    return movementRepo.findByCreditIdOrderByTxnAtDesc(id)
                            .collectList()
                            .flatMap(movementRepo::deleteAll)
                            .then(creditRepo.deleteById(id));
                });
    }

    @Override
    public Mono<CreditMovement> applyPayment(String id, CreditPaymentRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(creditRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var balance = nvl(doc.getBalance(), BigDecimal.ZERO);
                    if (req.getAmount().compareTo(balance) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Payment exceeds balance"));
                    }
                    var newBal = balance.subtract(req.getAmount());
                    String channel = (req.getChannel() != null) ? req.getChannel().getValue() : null;

                    return creditLedger.updateBalanceAndRecord(
                            doc, newBal, "PAYMENT", req.getAmount(), channel
                    );
                });
    }


    @Override
    public Mono<CreditMovement> applyCharge(String id, CreditChargeRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(creditRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var limit    = nvl(doc.getLimit(), BigDecimal.ZERO);
                    var balance  = nvl(doc.getBalance(), BigDecimal.ZERO);
                    var available= limit.subtract(balance);
                    boolean isCard = CreditType.CREDIT_CARD.name().equalsIgnoreCase(doc.getType());

                    if (isCard && req.getAmount().compareTo(available) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CREDIT_CARD_LIMIT_EXCEEDED"));
                    }

                    var newBal = balance.add(req.getAmount());
                    if (!isCard && newBal.compareTo(limit) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "New balance exceeds limit"));
                    }

                    String channel = hasText(req.getChannel()) ? req.getChannel() : "CARD";
                    return creditLedger.updateBalanceAndRecord(
                            doc, newBal, "CHARGE", req.getAmount(), channel
                    );
                });
    }

    @Override
    public Flux<CreditResponse> searchByDocument(String documentType, String documentNumber) {
        return customerClient.resolveCustomerId(documentType, documentNumber.trim())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
                .flatMapMany(cid -> creditRepo.findByCustomerId(cid))
                .map(CreditMapper::toApi);
    }






}
