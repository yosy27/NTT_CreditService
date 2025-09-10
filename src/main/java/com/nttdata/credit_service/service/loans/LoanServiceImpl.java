package com.nttdata.credit_service.service.loans;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.client.CustomerClient;
import com.nttdata.credit_service.domain.LoanDocument;
import com.nttdata.credit_service.repository.LoanRepository;
import com.nttdata.credit_service.repository.MovementRepository;
import com.nttdata.credit_service.service.LoanService;
import com.nttdata.credit_service.service.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.nttdata.credit_service.shared.util.Numbers.nz;
import static com.nttdata.credit_service.shared.validation.Validators.requireNonNegativeMono;
import static com.nttdata.credit_service.shared.validation.Validators.requirePositiveMono;
import static java.time.Instant.now;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepo;
    private final MovementRepository movementRepo;
    private final CustomerClient customerClient;
    private final LoanCreationPolicy loanCreationPolicy;
    private final LoanLedger ledger;

    @Override
    public Mono<LoanResponse> registerLoan(LoanRequest req) {
        if (req == null || req.getType() == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "type is required"));
        }
        // limit >= 0, interestAnnual >= 0
        return requireNonNegativeMono(req.getLimit(), "limit")
                .then(requireNonNegativeMono(req.getInterestAnnual(), "interestAnnual"))
                // no permitir ambos identificadores a la vez
                .then(Mono.defer(() -> {
                    if (hasText(req.getCustomerId()) && req.getDocumentType() != null && hasText(req.getDocumentNumber())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Provide only customerId OR documentType+documentNumber, not both"));
                    }
                    // resolver customerId
                    Mono<String> customerIdMono;
                    if (hasText(req.getCustomerId())) {
                        var cid = req.getCustomerId().trim();
                        customerIdMono = customerClient.existsCustomerId(cid)
                                .flatMap(exists -> exists ? Mono.just(cid)
                                        : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")));
                    } else if (req.getDocumentType() != null && hasText(req.getDocumentNumber())) {
                        var docType = req.getDocumentType().name();
                        var docNum  = req.getDocumentNumber().trim();
                        customerIdMono = customerClient.resolveCustomerId(docType, docNum)
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")));
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Provide customerId OR documentType+documentNumber"));
                    }

                    // admisión + persistencia
                    return customerIdMono
                            .flatMap(cid -> loanCreationPolicy.validate(cid, req.getType())
                                    .then(Mono.defer(() -> loanRepo.save(LoanMapper.fromCreate(req, cid)))))
                            .map(LoanMapper::toApi);
                }));
    }

    @Override
    public Flux<LoanResponse> listLoans(String customerId, LoanType type, CreditStatus status, Boolean includeClosed) {
        final boolean incClosed = Boolean.TRUE.equals(includeClosed);
        Flux<LoanDocument> stream = hasText(customerId)
                ? loanRepo.findByCustomerId(customerId.trim())
                : loanRepo.findAll();

        if (type != null) {
            final String typeStr = type.name();
            stream = stream.filter(d -> typeStr.equals(d.getType()));
        }
        if (status != null) {
            final String st = status.name();
            stream = stream.filter(d -> st.equals(d.getStatus()));
        } else if (!incClosed) {
            stream = stream.filter(d -> !CreditStatus.CLOSED.name().equals(d.getStatus()));
        }

        return stream.map(LoanMapper::toApi);
    }

    @Override
    public Mono<LoanResponse> getLoan(String id) {
        return loanRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .map(LoanMapper::toApi);
    }

    @Override
    public Mono<LoanResponse> updateLoan(String id, LoanRequest req) {
        return loanRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    LoanMapper.mergeAll(doc, req);   // sólo campos editables completos
                    doc.setUpdatedAt(now());
                    return loanRepo.save(doc);
                })
                .map(LoanMapper::toApi);
    }

    @Override
    public Mono<LoanResponse> patchLoan(String id, LoanUpdate patch) {
        return loanRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    LoanMapper.merge(doc, patch);    // sólo campos permitidos en PATCH
                    doc.setUpdatedAt(now());
                    return loanRepo.save(doc);
                })
                .map(LoanMapper::toApi);
    }

    @Override
    public Mono<Void> deleteLoan(String id) {
        // Borra movimientos asociados y luego el préstamo
        return loanRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.empty();
                    return movementRepo.findByCreditIdOrderByTxnAtDesc(id)
                            .collectList()
                            .flatMap(movementRepo::deleteAll)
                            .then(loanRepo.deleteById(id));
                });
    }

    @Override
    public Mono<LoanResponse> closeLoan(String id, String reason) {
        return loanRepo.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var bal = nz(doc.getBalance());
                    if (bal.compareTo(BigDecimal.ZERO) > 0) {
                        // Regla: sólo se cierra con balance 0
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "CREDIT_INVALID_STATE"));
                    }
                    if (CreditStatus.CLOSED.name().equals(doc.getStatus())) {
                        return Mono.just(doc); // idempotente
                    }
                    doc.setStatus(CreditStatus.CLOSED.name());
                    doc.setClosedAt(now());
                    doc.setCloseReason(hasText(reason) ? reason.trim() : null);
                    doc.setUpdatedAt(now());
                    return loanRepo.save(doc);
                })
                .map(LoanMapper::toApi);
    }

    @Override
    public Mono<LoanResponse> adjustLoanLimit(String id, BigDecimal newLimit, String reason) {
        return requireNonNegativeMono(newLimit, "newLimit")
                .then(loanRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var balance = nz(doc.getBalance());
                    if (newLimit.compareTo(balance) < 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "New limit < current balance"));
                    }
                    doc.setLimit(newLimit);
                    doc.setUpdatedAt(now());
                    return loanRepo.save(doc);
                })
                .map(LoanMapper::toApi);
    }

    @Override
    public Mono<CreditMovement> applyLoanPayment(String id, CreditPaymentRequest req) {
        return requirePositiveMono(req != null ? req.getAmount() : null, "amount")
                .then(loanRepo.findById(id))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CREDIT_NOT_FOUND")))
                .flatMap(doc -> {
                    var balance = nz(doc.getBalance());
                    if (req.getAmount().compareTo(balance) > 0) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Payment exceeds balance"));
                    }
                    var newBal = balance.subtract(req.getAmount());
                    String channel = (req.getChannel() != null) ? req.getChannel().getValue() : null;
                    return ledger.updateBalanceAndRecord(doc, newBal, "PAYMENT", req.getAmount(), channel);
                });
    }

    @Override
    public Flux<LoanResponse> searchLoansByDocument(DocumentType documentType, String documentNumber) {
        if (documentType == null || !hasText(documentNumber)) {
            return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "documentType and documentNumber are required"));
        }
        return customerClient.resolveCustomerId(documentType.name(), documentNumber.trim())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CUSTOMER_NOT_FOUND")))
                .flatMapMany(cid -> loanRepo.findByCustomerId(cid))
                .map(LoanMapper::toApi);
    }
}
