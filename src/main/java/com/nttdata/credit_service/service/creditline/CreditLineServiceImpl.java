package com.nttdata.credit_service.service.creditline;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.domain.CreditLineDocument;
import com.nttdata.credit_service.repository.CreditLineRepository;
import com.nttdata.credit_service.repository.MovementRepository;
import com.nttdata.credit_service.service.CreditLineService;
import com.nttdata.credit_service.service.mapper.CreditLineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

//import static com.nttdata.credit_service.model.util.CreditUtils.enumName;
//import static com.nttdata.credit_service.model.util.CreditUtils.nz;
import static com.nttdata.credit_service.shared.util.Numbers.enumName;
import static com.nttdata.credit_service.shared.util.Numbers.nz;
import static com.nttdata.credit_service.shared.validation.Validators.requireNonNegativeMono;
import static com.nttdata.credit_service.shared.validation.Validators.requirePositiveMono;
import static java.time.Instant.now;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class CreditLineServiceImpl implements CreditLineService {

    private final CreditLineRepository lineRepo;
    private final MovementRepository movementRepo;            // movimientos compartidos
    private final CustomerClient customerClient;              // por si lo necesitas en algún flujo
    private final CreditLineCreationPolicy creationPolicy;    // usa CommonAdmissionPolicy por dentro
    private final CreditLineLedger ledger;                    // aplica saldo + registra movimiento
    @Override
    public Mono<CreditLineResponse> createCreditLine(CreditLineRequest req) {
        // Validaciones básicas del payload (según OpenAPI)
        if (req == null
                || req.getCustomerId() == null
                || req.getLimit() == null
                || req.getInterestAnnual() == null
                || req.getBillingCycleDay() == null
                || req.getPaymentDueDay() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields"));
        }
        if (req.getLimit().compareTo(BigDecimal.ZERO) < 0 || req.getInterestAnnual().compareTo(BigDecimal.ZERO) < 0) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "limit and interestAnnual must be >= 0"));
        }
        if (req.getBillingCycleDay() < 1 || req.getBillingCycleDay() > 28
                || req.getPaymentDueDay() < 1 || req.getPaymentDueDay() > 28) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "billing/payment day out of range"));
        }

        final String customerId = req.getCustomerId().trim();

        // 1) Admisión transversal (cliente existe + no moroso)
        return creationPolicy.validate(customerId)

                // 2) Crear documento desde el request y persistir
                .then(Mono.defer(() -> lineRepo.save(CreditLineMapper.fromCreate(req))))
                .map(CreditLineMapper::toApi);
    }

    @Override
    public Flux<CreditLineResponse> listCreditLines(String customerId, CreditStatus status) {
        Flux<CreditLineDocument> stream =
                hasText(customerId) ? lineRepo.findByCustomerId(customerId.trim())
                        : lineRepo.findAll();

        if (status != null) {
            final String st = enumName(status);
            stream = stream.filter(d -> st.equals(d.getStatus()));
        }
        return stream.map(CreditLineMapper::toApi);
    }

    @Override
    public Mono<CreditLineResponse> getCreditLine(String id) {
        return lineRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .map(CreditLineMapper::toApi);
    }

    @Override
    public Mono<CreditLineResponse> updateCreditLine(String id, CreditLineRequest req) {
        if (req == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body required"));
        }
        return lineRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    CreditLineMapper.mergeAll(doc, req); // solo campos editables según tu mapper
                    doc.setUpdatedAt(now());
                    return lineRepo.save(doc);
                })
                .map(CreditLineMapper::toApi);
    }

    @Override
    public Mono<CreditLineResponse> patchCreditLine(String id, CreditLineUpdate patch) {
        if (patch == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body required"));
        }
        return lineRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    CreditLineMapper.merge(doc, patch); // interestAnnual / billingCycleDay / paymentDueDay
                    doc.setUpdatedAt(now());
                    return lineRepo.save(doc);
                })
                .map(CreditLineMapper::toApi);
    }

    @Override
    public Mono<Void> deleteCreditLine(String id) {
        return lineRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.empty();
                    return movementRepo.findByCreditIdOrderByTxnAtDesc(id)
                            .collectList()
                            .flatMap(movementRepo::deleteAll)
                            .then(lineRepo.deleteById(id));
                });
    }

    @Override
    public Mono<CreditLineResponse> adjustCreditLineLimit(String id, BigDecimal newLimit, String reason) {
        return requireNonNegativeMono(newLimit, "newLimit")
                .then(lineRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var balance = nz(doc.getBalance());
                    if (newLimit.compareTo(balance) < 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "New limit < current balance"));
                    }
                    doc.setLimit(newLimit);
                    doc.setUpdatedAt(now());
                    return lineRepo.save(doc);
                })
                .map(CreditLineMapper::toApi);
    }

    @Override
    public Mono<CreditMovement> postCharge(String id, CreditChargeRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(lineRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var limit     = nz(doc.getLimit());
                    var balance   = nz(doc.getBalance());
                    var available = limit.subtract(balance);

                    if (req.getAmount().compareTo(available) > 0) {
                        // Regla OpenAPI: devolver 422 si el cargo excede el disponible
                        return Mono.error(new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "CREDIT_CARD_LIMIT_EXCEEDED"));
                    }

                    var newBal = balance.add(req.getAmount());
                    String channel = hasText(req.getChannel()) ? req.getChannel() : "CARD";

                    return ledger.updateBalanceAndRecord(
                            doc, newBal, "CHARGE", req.getAmount(), channel
                    );
                });
    }

    @Override
    public Mono<CreditMovement> postPayment(String id, CreditPaymentRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(lineRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var balance = nz(doc.getBalance());
                    if (req.getAmount().compareTo(balance) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Payment exceeds balance"));
                    }
                    var newBal = balance.subtract(req.getAmount());
                    String channel = (req.getChannel() != null) ? req.getChannel().getValue() : null;

                    return ledger.updateBalanceAndRecord(
                            doc, newBal, "PAYMENT", req.getAmount(), channel
                    );
                });
    }

    @Override
    public Mono<CreditLineResponse> closeCreditLine(String id, String reason) {
        return lineRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var bal = nz(doc.getBalance());
                    if (bal.compareTo(BigDecimal.ZERO) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CREDIT_INVALID_STATE"));
                    }
                    if (CreditStatus.CLOSED.name().equals(doc.getStatus())) {
                        return Mono.just(doc); // idempotente
                    }
                    doc.setStatus(CreditStatus.CLOSED.name());
                    doc.setClosedAt(now());
                    doc.setCloseReason(hasText(reason) ? reason.trim() : null);
                    doc.setUpdatedAt(now());
                    return lineRepo.save(doc);
                })
                .map(CreditLineMapper::toApi);
    }
}
