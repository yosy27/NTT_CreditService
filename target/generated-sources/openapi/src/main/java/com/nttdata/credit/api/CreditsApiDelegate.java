package com.nttdata.credit.api;

import com.nttdata.credit.model.CreditCloseRequest;
import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit.model.CreditPaymentRequest;
import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.DocumentType;
import com.nttdata.credit.model.Error;
import com.nttdata.credit.model.InlineObject;
import com.nttdata.credit.model.LoanRequest;
import com.nttdata.credit.model.LoanResponse;
import com.nttdata.credit.model.LoanType;
import com.nttdata.credit.model.LoanUpdate;
import com.nttdata.credit.model.OverdueStatus;
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
 * A delegate to be called by the {@link CreditsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public interface CreditsApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * PATCH /credits/{id}/limit : Adjust loan limit
     * New limit must be ≥ current balance. 
     *
     * @param id Identificador único del crédito. (required)
     * @param inlineObject  (required)
     * @return Limit updated (status code 200)
     *         or Validation or bad request. (status code 400)
     *         or Resource not found. (status code 404)
     *         or Business rule violated. (status code 409)
     * @see CreditsApi#adjustLoanLimit
     */
    default Mono<ResponseEntity<LoanResponse>> adjustLoanLimit(String id,
        Mono<InlineObject> inlineObject,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credits/{id}/payments : Apply loan payment
     * Register a payment (owner or third-party). #Pago a préstamo 
     *
     * @param id  (required)
     * @param creditPaymentRequest  (required)
     * @return Payment posted (status code 201)
     *         or Validation or bad request. (status code 400)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditsApi#applyLoanPayment
     */
    default Mono<ResponseEntity<CreditMovement>> applyLoanPayment(String id,
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
     * POST /credits/{id}/close : Close loan (logical delete)
     * Close the loan by performing a logical deletion. 
     *
     * @param id Unique credit identifier. (required)
     * @param creditCloseRequest  (optional)
     * @return Loan closed (status code 200)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditsApi#closeLoan
     */
    default Mono<ResponseEntity<LoanResponse>> closeLoan(String id,
        Mono<CreditCloseRequest> creditCloseRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * DELETE /credits/{id} : Delete loan
     * Delete loan 
     *
     * @param id Loan id. (required)
     * @return Delete (status code 204)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditsApi#deleteLoan
     */
    default Mono<ResponseEntity<Void>> deleteLoan(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        return result.then(Mono.empty());

    }

    /**
     * GET /credits/overdue/{customerId} : Customer overdue status
     * Returns consolidated overdue info across all credit products.
     *
     * @param customerId Customer identifier. (required)
     * @return Customer Debt Summary (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditsApi#getDebtStatusByCustomer
     */
    default Mono<ResponseEntity<OverdueStatus>> getDebtStatusByCustomer(String customerId,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"hasOverdue\" : true, \"totalOverdueAmount\" : 0.8008281904610115 }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credits/{id} : Get loan by ID
     *
     * @param id Loan id. (required)
     * @return Loan found (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditsApi#getLoan
     */
    default Mono<ResponseEntity<LoanResponse>> getLoan(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credits : List loans
     * Returns loans. Use filters to scope the result. # Listado global con filtros 
     *
     * @param customerId Filter by customer id. (optional)
     * @param type Filter by loan type. (optional)
     * @param status Filter by status. (optional)
     * @param includeClosed Include CLOSED loans. (optional, default to false)
     * @return Loan list (status code 200)
     * @see CreditsApi#listLoans
     */
    default Mono<ResponseEntity<Flux<LoanResponse>>> listLoans(String customerId,
        LoanType type,
        CreditStatus status,
        Boolean includeClosed,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PATCH /credits/{id} : Partial update
     * Updates allowed fields only. 
     *
     * @param id Loan id. (required)
     * @param loanUpdate  (required)
     * @return Loan updated (status code 200)
     *         or Validation or bad request. (status code 400)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditsApi#patchLoan
     */
    default Mono<ResponseEntity<LoanResponse>> patchLoan(String id,
        Mono<LoanUpdate> loanUpdate,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * POST /credits : Create loan
     * Creates a PERSONAL or BUSINESS loan.  Rules:  - PERSONAL: only one active loan per customer.  - BUSINESS: multiple loans allowed. - Admission: reject if customer has overdue debts. 
     *
     * @param loanRequest  (required)
     * @return Loan created (status code 201)
     *         or Validation or bad request. (status code 400)
     *         or Conflict with business rules or resource state. (status code 409)
     *         or Semantically valid but violates domain constraints (status code 422)
     * @see CreditsApi#registerLoan
     */
    default Mono<ResponseEntity<LoanResponse>> registerLoan(Mono<LoanRequest> loanRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credits/search : Search loans by customer document
     * Returns loans for the provided document 
     *
     * @param documentType Tipo de documento (DNI, RUC, CE). (required)
     * @param documentNumber  (required)
     * @return Loans found (status code 200)
     *         or Resource not found. (status code 404)
     *         or Dependent service unavailable (e.g., customer-service) (status code 503)
     * @see CreditsApi#searchLoansByDocument
     */
    default Mono<ResponseEntity<Flux<LoanResponse>>> searchLoansByDocument(DocumentType documentType,
        String documentNumber,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * PUT /credits/{id} : Update Loan
     * Update Loan
     *
     * @param id Loan id. (required)
     * @param loanRequest  (required)
     * @return Loan updated (status code 200)
     *         or Validation or bad request. (status code 400)
     *         or Resource not found. (status code 404)
     *         or Conflict with business rules or resource state. (status code 409)
     * @see CreditsApi#updateLoan
     */
    default Mono<ResponseEntity<LoanResponse>> updateLoan(String id,
        Mono<LoanRequest> loanRequest,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"dueDate\" : \"2000-01-23\", \"interestAnnual\" : 0.14658129805029452, \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 0.6027456183070403, \"customerId\" : \"customerId\", \"limit\" : 0.08008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\", \"closedAt\" : \"2000-01-23T04:56:07.000+00:00\", \"closeReason\" : \"closeReason\", \"updatedAt\" : \"2000-01-23T04:56:07.000+00:00\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

}
