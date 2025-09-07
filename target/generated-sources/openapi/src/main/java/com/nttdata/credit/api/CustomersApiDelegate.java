package com.nttdata.credit.api;

import com.nttdata.credit.model.CreditProductSummary;
import com.nttdata.credit.model.CreditStatus;
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
 * A delegate to be called by the {@link CustomersApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public interface CustomersApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /customers/{customerId}/credit-products : List customer credit products (loans + credit lines)
     *
     * @param customerId  (required)
     * @param kind  (optional)
     * @param status  (optional)
     * @return Product list (status code 200)
     * @see CustomersApi#listCreditProductsByCustomer
     */
    default Mono<ResponseEntity<Flux<CreditProductSummary>>> listCreditProductsByCustomer(String customerId,
        String kind,
        CreditStatus status,
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

}
