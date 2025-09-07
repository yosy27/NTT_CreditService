package com.nttdata.credit.api;

import com.nttdata.credit.model.CreditChargeRequest;
import com.nttdata.credit.model.CreditLineRequest;
import com.nttdata.credit.model.CreditLineResponse;
import com.nttdata.credit.model.CreditLineUpdate;
import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit.model.CreditPaymentRequest;
import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.Error;
import com.nttdata.credit.model.InlineObject1;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.codec.multipart.Part;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A delegate to be called by the {@link CreditLinesApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public interface CreditLinesApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * PATCH /credit-lines/{id}/limit : Adjust credit line limit
     * New limit must be ≥ current balance.
     *
     * @param id  (required)
     * @param inlineObject1  (required)
     * @return Limit updated (status code 200)
     * @see CreditLinesApi#adjustCreditLineLimit
     */
    default Mono<ResponseEntity<CreditLineResponse>> adjustCreditLineLimit(String id,
        Mono<InlineObject1> inlineObject1,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credit-lines/{id}/close : Close credit line (logical delete)
     * Requires balance&#x3D;0.
     *
     * @param id  (required)
     * @return Credit line closed (status code 200)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditLinesApi#closeCreditLine
     */
    default Mono<ResponseEntity<CreditLineResponse>> closeCreditLine(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credit-lines : Create credit line
     * Creates a credit line used by credit cards.
     *
     * @param creditLineRequest  (required)
     * @return Credit line created (status code 201)
     * @see CreditLinesApi#createCreditLine
     */
    default Mono<ResponseEntity<CreditLineResponse>> createCreditLine(Mono<CreditLineRequest> creditLineRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * DELETE /credit-lines/{id} : Delete credit line
     *
     * @param id  (required)
     * @return Deleted (status code 204)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditLinesApi#deleteCreditLine
     */
    default Mono<ResponseEntity<Void>> deleteCreditLine(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(Mono.empty());

    }

    /**
     * GET /credit-lines/{id} : Get credit line by ID
     *
     * @param id  (required)
     * @return Credit line (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditLinesApi#getCreditLine
     */
    default Mono<ResponseEntity<CreditLineResponse>> getCreditLine(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credit-lines : List credit lines
     *
     * @param customerId  (optional)
     * @param status  (optional)
     * @return Credit line list (status code 200)
     * @see CreditLinesApi#listCreditLines
     */
    default Mono<ResponseEntity<Flux<CreditLineResponse>>> listCreditLines(String customerId,
        CreditStatus status,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PATCH /credit-lines/{id} : Partial update (credit line)
     *
     * @param id  (required)
     * @param creditLineUpdate  (required)
     * @return Credit line updated (status code 200)
     *         or Validation or bad request. (status code 400)
     * @see CreditLinesApi#patchCreditLine
     */
    default Mono<ResponseEntity<CreditLineResponse>> patchCreditLine(String id,
        Mono<CreditLineUpdate> creditLineUpdate,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credit-lines/{id}/charges : Post charge (card purchase)
     * Validates available amount before posting.
     *
     * @param id  (required)
     * @param creditChargeRequest  (required)
     * @return Charge posted (status code 201)
     *         or Semantically valid but violates domain constraints (status code 422)
     * @see CreditLinesApi#postCreditLineCharge
     */
    default Mono<ResponseEntity<CreditMovement>> postCreditLineCharge(String id,
        Mono<CreditChargeRequest> creditChargeRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"creditId\" : \"creditId\", \"amount\" : 0.8008281904610115, \"postedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"txnAt\" : \"2000-01-23T04:56:07.000+00:00\", \"channel\" : \"channel\", \"id\" : \"id\", \"type\" : \"CHARGE\", \"runningBalance\" : 6.027456183070403 }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credit-lines/{id}/payments : Post payment to the credit line
     *
     * @param id  (required)
     * @param creditPaymentRequest  (required)
     * @return Payment posted (status code 201)
     * @see CreditLinesApi#postCreditLinePayment
     */
    default Mono<ResponseEntity<CreditMovement>> postCreditLinePayment(String id,
        Mono<CreditPaymentRequest> creditPaymentRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"creditId\" : \"creditId\", \"amount\" : 0.8008281904610115, \"postedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"txnAt\" : \"2000-01-23T04:56:07.000+00:00\", \"channel\" : \"channel\", \"id\" : \"id\", \"type\" : \"CHARGE\", \"runningBalance\" : 6.027456183070403 }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PUT /credit-lines/{id} : Replace credit line
     *
     * @param id  (required)
     * @param creditLineRequest  (required)
     * @return Credit line replaced (status code 200)
     * @see CreditLinesApi#updateCreditLine
     */
    default Mono<ResponseEntity<CreditLineResponse>> updateCreditLine(String id,
        Mono<CreditLineRequest> creditLineRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"paymentDueDay\" : 16, \"balance\" : 0.6027456183070403, \"billingCycleDay\" : 17, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

}
