package com.nttdata.credit_service.api.delegate;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.api.LoansApiDelegate;
import com.nttdata.credit_service.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LoansApiDelegateImpl implements LoansApiDelegate {

    private final LoanService service;

    @Override
    public Mono<ResponseEntity<Flux<LoanResponse>>> listLoans(String customerId, LoanType type, CreditStatus status, Boolean includeClosed, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(
                service.listLoans(customerId, type, status, includeClosed)));
    }

    @Override
    public Mono<ResponseEntity<Flux<LoanResponse>>> searchLoansByDocument(DocumentType documentType, String documentNumber, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(
                service.searchLoansByDocument(documentType, documentNumber)));
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> getLoan(String id, ServerWebExchange exchange) {
        return service.getLoan(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> registerLoan(Mono<LoanRequest> loanRequest, ServerWebExchange exchange) {
        return loanRequest.flatMap(service::registerLoan)
                .map(created -> ResponseEntity.created(URI.create("/credits/" + created.getId()))
                        .body(created));
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> patchLoan(String id, Mono<LoanUpdate> loanUpdate, ServerWebExchange exchange) {
        return loanUpdate.flatMap(p -> service.patchLoan(id, p))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteLoan(String id, ServerWebExchange exchange) {
        return service.deleteLoan(id).thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> closeLoan(String id, Mono<CreditCloseRequest> creditCloseRequest, ServerWebExchange exchange) {
        return creditCloseRequest.defaultIfEmpty(new CreditCloseRequest())
                .flatMap(b -> service.closeLoan(id, b.getReason()))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CreditMovement>> applyLoanPayment(String id, Mono<CreditPaymentRequest> creditPaymentRequest, ServerWebExchange exchange) {
        return creditPaymentRequest.flatMap(b -> service.applyLoanPayment(id, b))
                .map(m -> ResponseEntity.created(URI.create("/credits/" + id + "/movements/" + m.getId()))
                        .body(m));
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> adjustLoanLimit(String id, Mono<AdjustLoanLimitRequest> adjustLoanLimitRequest, ServerWebExchange exchange) {
        return adjustLoanLimitRequest.flatMap(b -> service.adjustLoanLimit(id, b.getNewLimit(), b.getReason()))
               .map(ResponseEntity::ok);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return LoansApiDelegate.super.getRequest();
    }

    @Override
    public Mono<ResponseEntity<LoanResponse>> updateLoan(String id, Mono<LoanRequest> loanRequest, ServerWebExchange exchange) {
        return loanRequest.flatMap(req -> service.updateLoan(id, req))
                .map(ResponseEntity::ok);
    }
}
