package com.nttdata.credit_service.service;

import com.nttdata.credit.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface LoanService {

    /** POST /credits */
    Mono<LoanResponse> registerLoan(LoanRequest req);

    /** GET /credits?customerId&type&status&includeClosed */
    Flux<LoanResponse> listLoans(String customerId, LoanType type, CreditStatus status, Boolean includeClosed);

    /** GET /credits/{id} */
    Mono<LoanResponse> getLoan(String id);

    /** PUT /credits/{id} */
    Mono<LoanResponse> updateLoan(String id, LoanRequest req);

    /** PATCH /credits/{id} */
    Mono<LoanResponse> patchLoan(String id, LoanUpdate patch);

    /** DELETE /credits/{id} */
    Mono<Void> deleteLoan(String id);

    /** POST /credits/{id}/close */
    Mono<LoanResponse> closeLoan(String id, String reason);

    /** PATCH /credits/{id}/limit */
    Mono<LoanResponse> adjustLoanLimit(String id, BigDecimal newLimit, String reason);

    /** POST /credits/{id}/payments */
    Mono<CreditMovement> applyLoanPayment(String id, CreditPaymentRequest req);

    /** GET /credits/search?documentType&documentNumber */
    Flux<LoanResponse> searchLoansByDocument(DocumentType documentType, String documentNumber);
}
