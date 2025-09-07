package com.nttdata.credit.api;

import com.nttdata.credit.model.CreditBalance;
import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit.model.CreditProductSummary;
import com.nttdata.credit.model.Error;
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
 * A delegate to be called by the {@link CreditProductsApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public interface CreditProductsApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /credit-products/{id} : Get credit product (loan or credit line)
     *
     * @param id  (required)
     * @return Credit product summary (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditProductsApi#getCreditProduct
     */
    default Mono<ResponseEntity<CreditProductSummary>> getCreditProduct(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"createdAt\" : \"2000-01-23T04:56:07.000+00:00\", \"balance\" : 6.027456183070403, \"kind\" : \"LOAN\", \"limit\" : 0.8008281904610115, \"currency\" : \"PEN\", \"id\" : \"id\" }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credit-products/{id}/balance : Get product balance
     *
     * @param id  (required)
     * @return OK (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditProductsApi#getCreditProductBalance
     */
    default Mono<ResponseEntity<CreditBalance>> getCreditProductBalance(String id,
        ServerWebExchange exchange) {
        Mono<Void> result = Mono.empty();
        exchange.getResponse().setStatusCode(HttpStatus.NOT_IMPLEMENTED);
        for (MediaType mediaType : exchange.getRequest().getHeaders().getAccept()) {
            if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                String exampleString = "{ \"balance\" : 0.6027456183070403, \"limit\" : 0.08008281904610115, \"available\" : 1.4658129805029452 }";
                result = ApiUtil.getExampleResponse(exchange, exampleString);
                break;
            }
        }
        return result.then(Mono.empty());

    }

    /**
     * GET /credit-products/{id}/movements : List product movements
     *
     * @param id  (required)
     * @param type  (optional)
     * @param limit  (optional, default to 10)
     * @return Movement list (status code 200)
     *         or Resource not found. (status code 404)
     * @see CreditProductsApi#listCreditProductMovements
     */
    default Mono<ResponseEntity<Flux<CreditMovement>>> listCreditProductMovements(String id,
        String type,
        Integer limit,
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

}
