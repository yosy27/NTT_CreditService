package com.nttdata.credit_service.loggin;

import com.nttdata.credit_service.logging.RequestLoggingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RequestLoggingFilterTest {

    private static final String HDR_CORR = "X-Correlation-Id";
    private static final String CORR_ID = "corrId";
    private static final String REQ_ID  = "reqId";

    @Test
    void whenHeaderMissing_generatesCorrelationId_andAddsToResponse_successFlow() {
        // Request SIN header de correlación
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/test?x=1")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        RequestLoggingFilter filter = new RequestLoggingFilter();

        // Cadena exitosa (completa sin error)
        WebFilterChain chain = new WebFilterChain() {
            @Override public Mono<Void> filter(ServerWebExchange e) {
                // Simular que el handler seteó un status 204
                e.getResponse().setStatusCode(HttpStatus.NO_CONTENT);
                return Mono.empty();
            }
        };

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        // Header de respuesta debe contener corrId generado (UUID)
        String corrInResponse = exchange.getResponse().getHeaders().getFirst(HDR_CORR);
        assertNotNull(corrInResponse);
        assertFalse(corrInResponse.isBlank());
        assertDoesNotThrow(() -> UUID.fromString(corrInResponse));

        // Atributos deben haberse poblado en el exchange
        Map<String,Object> attrs = exchange.getAttributes();
        assertEquals(corrInResponse, attrs.get(CORR_ID));
        assertNotNull(attrs.get(REQ_ID)); // requestId generado por WebFlux en MockServerHttpRequest
    }

    @Test
    void whenHeaderPresent_preservesCorrelationId_andAddsToResponse_successFlow() {
        String providedCorr = UUID.randomUUID().toString();

        // Request CON header de correlación
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/health")
                .header(HDR_CORR, providedCorr)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        RequestLoggingFilter filter = new RequestLoggingFilter();

        // Cadena exitosa
        WebFilterChain chain = e -> Mono.empty();

        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        // Debe preservarse el corrId proporcionado
        String corrInResponse = exchange.getResponse().getHeaders().getFirst(HDR_CORR);
        assertEquals(providedCorr, corrInResponse);

        // Atributos OK
        assertEquals(providedCorr, exchange.getAttribute(CORR_ID));
        assertNotNull(exchange.getAttribute(REQ_ID));
    }

    @Test
    void whenChainErrors_stillAddsCorrelationHeader_andPropagatesError() {
        MockServerHttpRequest request = MockServerHttpRequest
                .post("/api/boom")
                .header(HttpHeaders.ACCEPT, "application/json")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        RequestLoggingFilter filter = new RequestLoggingFilter();

        // Cadena que falla
        RuntimeException expected = new RuntimeException("boom!");
        WebFilterChain chain = e -> Mono.error(expected);

        StepVerifier.create(filter.filter(exchange, chain))
                .expectErrorMatches(err -> err == expected)
                .verify();

        // A pesar del error, el header de correlación debe existir
        String corrInResponse = exchange.getResponse().getHeaders().getFirst(HDR_CORR);
        assertNotNull(corrInResponse);
        assertFalse(corrInResponse.isBlank());
        assertDoesNotThrow(() -> UUID.fromString(corrInResponse));

        // Atributos presentes
        assertEquals(corrInResponse, exchange.getAttribute(CORR_ID));
        assertNotNull(exchange.getAttribute(REQ_ID));
    }

    @Test
    void pathAndQueryCaptured_doNotBreak_whenNoQueryOrMethodIsNullSafe() {
        // Petición sin query string
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/simple")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        RequestLoggingFilter filter = new RequestLoggingFilter();
        WebFilterChain chain = e -> Mono.empty();

        // No debe explotar aunque el query sea null -> código lo maneja como "-"
        StepVerifier.create(filter.filter(exchange, chain))
                .verifyComplete();

        // Solo chequeo de sanidad del header/atributos
        String corr = exchange.getResponse().getHeaders().getFirst(HDR_CORR);
        assertNotNull(corr);
        assertFalse(corr.isBlank());
    }
}
