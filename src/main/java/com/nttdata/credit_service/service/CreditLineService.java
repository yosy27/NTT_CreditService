package com.nttdata.credit_service.service;

import com.nttdata.credit.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CreditLineService {

    /** POST /credit-lines */
    Mono<CreditLineResponse> createCreditLine(CreditLineRequest req);

    /** GET /credit-lines?customerId&status */
    Flux<CreditLineResponse> listCreditLines(String customerId, CreditStatus status);

    /** GET /credit-lines/{id} */
    Mono<CreditLineResponse> getCreditLine(String id);

    /** PUT /credit-lines/{id} */
    Mono<CreditLineResponse> updateCreditLine(String id, CreditLineRequest req);

    /** PATCH /credit-lines/{id} */
    Mono<CreditLineResponse> patchCreditLine(String id, CreditLineUpdate patch);

    /** DELETE /credit-lines/{id}  (si decides exponerlo como CRUD completo) */
    Mono<Void> deleteCreditLine(String id);

    /** PATCH /credit-lines/{id}/limit */
    Mono<CreditLineResponse> adjustCreditLineLimit(String id, BigDecimal newLimit, String reason);

    /** POST /credit-lines/{id}/charges */
    Mono<CreditMovement> postCharge(String id, CreditChargeRequest req);

    /** POST /credit-lines/{id}/payments */
    Mono<CreditMovement> postPayment(String id, CreditPaymentRequest req);

    /** POST /credit-lines/{id}/close */
    Mono<CreditLineResponse> closeCreditLine(String id, String reason);
}
