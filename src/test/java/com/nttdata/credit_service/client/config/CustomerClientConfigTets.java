//package com.nttdata.credit_service.client.config;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.ExchangeFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.lang.reflect.Method;
//import java.util.concurrent.atomic.AtomicReference;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CustomerClientConfigTets {
//
//    private static WebClient clientCapturingUrl(WebClient baseClient, AtomicReference<String> capturedUrlRef) {
//        ExchangeFunction fx = request -> {
//            capturedUrlRef.set(request.url().toString());
//            // Responde 200 OK vacío
//            return Mono.just(ClientResponse.create(HttpStatus.OK).build());
//        };
//        return baseClient.mutate().exchangeFunction(fx).build();
//    }
//
//    @Test
//    void customerWebClient_builds_withBaseUrl_noTrailingSlash() {
//        String base = "http://localhost:8081/api/v1";
//        CustomerClientConfig cfg = new CustomerClientConfig();
//
//        WebClient beanClient = cfg.customerWebClient(base);
//        assertNotNull(beanClient);
//
//        AtomicReference<String> captured = new AtomicReference<>();
//        WebClient client = clientCapturingUrl(beanClient, captured);
//
//        // Dispara una request relativa para comprobar la resolución con baseUrl
//        StepVerifier.create(
//                client.get().uri("/customers").exchangeToMono(resp -> Mono.empty())
//        ).verifyComplete();
//
//        assertEquals("http://localhost:8081/api/v1/customers", captured.get());
//    }
//
//    @Test
//    void customerWebClient_builds_withBaseUrl_withTrailingSlash() {
//        String base = "http://localhost:8081/api/v1/"; // con slash final
//        CustomerClientConfig cfg = new CustomerClientConfig();
//
//        WebClient beanClient = cfg.customerWebClient(base);
//        assertNotNull(beanClient);
//
//        AtomicReference<String> captured = new AtomicReference<>();
//        WebClient client = clientCapturingUrl(beanClient, captured);
//
//        StepVerifier.create(
//                client.get().uri("health").exchangeToMono(resp -> Mono.empty())
//        ).verifyComplete();
//
//        assertEquals("http://localhost:8081/api/v1/health", captured.get());
//    }
//
//    @Test
//    void customerWebClient_invalidBaseUrl_throwsIllegalArgumentException() {
//        String invalid = "http: :// bad  uri"; // inválida
//        CustomerClientConfig cfg = new CustomerClientConfig();
//
//        assertThrows(IllegalArgumentException.class, () -> cfg.customerWebClient(invalid));
//    }
//
//    @Test
//    void customerWebClient_nullBase_throwsException() {
//        CustomerClientConfig cfg = new CustomerClientConfig();
//        assertThrows(RuntimeException.class, () -> {
//            try {
//                cfg.customerWebClient(null);
//            } catch (NullPointerException | IllegalArgumentException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    @Test
//    void methodHasBeanAnnotation() throws NoSuchMethodException {
//        Method m = CustomerClientConfig.class.getDeclaredMethod("customerWebClient", String.class);
//        assertTrue(m.isAnnotationPresent(Bean.class), "El método customerWebClient debe estar anotado con @Bean");
//    }
//
//}
