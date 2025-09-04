package com.nttdata.credit_service.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalErrorHandlerTest {
    private final GlobalErrorHandler handler = new GlobalErrorHandler();

    @Test
    void handleIllegalArgument_returnsNotFound() {
        IllegalArgumentException ex = new IllegalArgumentException("not found");
        StepVerifier.create(handler.handleIllegalArgument(ex))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
                    GlobalErrorHandler.ErrorBody body = resp.getBody();
                    assertNotNull(body);
                    assertEquals(404, body.getStatus());
                    assertEquals("Not Found", body.getError());
                    assertEquals("not found", body.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void handleAny_returnsInternalServerError() {
        Exception ex = new RuntimeException("boom");
        StepVerifier.create(handler.handleAny(ex))
                .assertNext(resp -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
                    GlobalErrorHandler.ErrorBody body = resp.getBody();
                    assertNotNull(body);
                    assertEquals(500, body.getStatus());
                    assertEquals("Internal Server Error", body.getError());
                    assertEquals("boom", body.getMessage());
                })
                .verifyComplete();
    }

}
