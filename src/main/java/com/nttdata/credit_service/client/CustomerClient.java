package com.nttdata.credit_service.client;

import com.nttdata.credit_service.client.customer.dto.CustomerIdDto;
import com.nttdata.credit_service.client.customer.dto.CustomerSummaryDto;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.timelimiter.TimeLimiterOperator;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerClient {

    @Qualifier("customerWebClient")
    // Inyecta el bean configurado arriba
    private final WebClient customerWebClient;
    private final CircuitBreakerRegistry cbRegistry;
    private final TimeLimiterRegistry tlRegistry;



    /** Resuelve customerId desde documentType + documentNumber usando /customers/eligibility */
    public Mono<String> resolveCustomerId(String documentType, String documentNumber) {
        return customerWebClient.get()
                .uri(u -> u.path("/customers/eligibility")
                        .queryParam("documentType", documentType)
                        .queryParam("documentNumber", documentNumber)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(CustomerIdDto.class)
                                .flatMap(er -> StringUtils.hasText(er.getId())
                                        ? Mono.just(er.getId())
                                        : Mono.empty());               // <-- nunca devolvemos null
                    }
                    if (resp.statusCode().value() == 404) return Mono.empty(); // cliente no existe
                    return resp.createException().flatMap(Mono::error);        // otros errores
                });
    }

    public Mono<Boolean> existsCustomerId(String customerId) {
        return customerWebClient.get()
                .uri("/customers/{id}", customerId)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) return Mono.just(true);
                    if (resp.statusCode().value() == 404)    return Mono.just(false);
                    return resp.createException().flatMap(Mono::error);
                });
    }

    public Mono<CustomerSummaryDto> getCustomerSummary(String customerId) {
        var cb = cbRegistry.circuitBreaker("customerClient"); // nombre = instances.customerClient
        var tl = tlRegistry.timeLimiter("customerClient");
        return customerWebClient.get()
                .uri("/customers/{id}", customerId)
                .retrieve()
                .bodyToMono(CustomerSummaryDto.class)
                .transformDeferred(CircuitBreakerOperator.of(cb)) // 1) Circuit Breaker
                .transformDeferred(TimeLimiterOperator.of(tl))    // 2) Timeout lógico 2s
                .onErrorResume(err -> fallbackCustomerSummary(customerId, err));
    }


    private Mono<CustomerSummaryDto> fallbackCustomerSummary(String id, Throwable cause) {
        var dto = new CustomerSummaryDto();
        dto.setId(id);
        dto.setType("PERSONAL");
        dto.setProfile("STANDARD");
        return Mono.just(dto);
    }
}
