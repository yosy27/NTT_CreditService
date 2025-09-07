//package com.nttdata.credit_service.client;
//
//
//import com.nttdata.credit_service.client.customer.dto.CustomerSummaryDto;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.ExchangeFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//import org.springframework.core.io.buffer.DefaultDataBufferFactory;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Objects;
//
//public class CustomerClientTest {
//
//    private WebClient stubClient() {
//        ExchangeFunction fx = request -> {
//            String path = request.url().getPath();
//            String query = request.url().getQuery();
//            HttpMethod method = request.method();
//
//            if (method == HttpMethod.GET && path.equals("/customers/eligibility")) {
//                // 200 con id válido
//                if (Objects.equals(query, "documentType=DNI&documentNumber=123")) {
//                    String json = "{\"id\":\"C1\"}";
//                    var buf = new DefaultDataBufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
//                    return Mono.just(ClientResponse.create(HttpStatus.OK)
//                            .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                            .body(Flux.just(buf))
//                            .build());
//                }
//                // 200 con id vacío
//                if (Objects.equals(query, "documentType=DNI&documentNumber=000")) {
//                    String json = "{\"id\":\"\"}";
//                    var buf = new DefaultDataBufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
//                    return Mono.just(ClientResponse.create(HttpStatus.OK)
//                            .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                            .body(Flux.just(buf))
//                            .build());
//                }
//                // 404
//                if (Objects.equals(query, "documentType=DNI&documentNumber=404")) {
//                    return Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build());
//                }
//                // 500
//                if (Objects.equals(query, "documentType=DNI&documentNumber=500")) {
//                    return Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build());
//                }
//            }
//
//            // ==== existsCustomerId & getCustomerSummary ====
//            if (method == HttpMethod.GET && path.equals("/customers/C1")) {
//                String json = "{\"id\":\"C1\",\"type\":\"PERSONAL\",\"profile\":\"STANDARD\"}";
//                var buf = new DefaultDataBufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
//                return Mono.just(ClientResponse.create(HttpStatus.OK)
//                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                        .body(Flux.just(buf))
//                        .build());
//            }
//            if (method == HttpMethod.GET && path.equals("/customers/C404")) {
//                return Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND).build());
//            }
//            if (method == HttpMethod.GET && path.equals("/customers/C500")) {
//                return Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build());
//            }
//
//            return Mono.just(ClientResponse.create(HttpStatus.BAD_REQUEST).build());
//        };
//        return WebClient.builder().exchangeFunction(fx).baseUrl("http://localhost").build();
//    }
//
//    @Test
//    void resolveCustomerId_ok_withId() {
//        CustomerClient client = newClientForTests();
//
//        StepVerifier.create(client.resolveCustomerId("DNI", "123"))
//                .expectNext("C1")
//                .verifyComplete();
//    }
//
//    @Test
//    void resolveCustomerId_ok_emptyId_returnsEmptyMono() {
//        CustomerClient client =  newClientForTests();
//
//        StepVerifier.create(client.resolveCustomerId("DNI", "000"))
//                .verifyComplete();
//    }
//
//    @Test
//    void resolveCustomerId_404_returnsEmptyMono() {
//        CustomerClient client =  newClientForTests();
//
//        StepVerifier.create(client.resolveCustomerId("DNI", "404"))
//                .verifyComplete();
//    }
//
//    @Test
//    void resolveCustomerId_serverError_propagates() {
//        CustomerClient client =  newClientForTests();
//
//        StepVerifier.create(client.resolveCustomerId("DNI", "500"))
//                .expectError()
//                .verify();
//    }
//
//    @Test
//    void existsCustomerId_true_false_and_error() {
//        CustomerClient client =  newClientForTests();
//
//        StepVerifier.create(client.existsCustomerId("C1"))
//                .expectNext(true)
//                .verifyComplete();
//
//        StepVerifier.create(client.existsCustomerId("C404"))
//                .expectNext(false)
//                .verifyComplete();
//
//        StepVerifier.create(client.existsCustomerId("C500"))
//                .expectError()
//                .verify();
//    }
//
//    @Test
//    void getCustomerSummary_ok_and_404_error() {
//        CustomerClient client =  newClientForTests();
//
//        StepVerifier.create(client.getCustomerSummary("C1"))
//                .assertNext(summary -> {
//                    assert summary != null;
//                    assert "C1".equals(summary.getId());
//                })
//                .verifyComplete();
//
//        StepVerifier.create(client.getCustomerSummary("C404"))
//                .expectError() // retrieve() con 404 lanza error
//                .verify();
//    }
//
//
//    private CustomerClient newClientForTests() {
//        var webClient = stubClient();
//        var cb = CircuitBreakerRegistry.ofDefaults();
//        var tl = TimeLimiterRegistry.ofDefaults();
//        return new CustomerClient(webClient, cb, tl);
//    }
//}
