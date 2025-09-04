package com.nttdata.credit_service.model.util;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

import java.math.BigDecimal;


public class ValidatorsTest {

    @Test
    void requirePositiveMono_valid() {
        StepVerifier.create(Validators.requirePositiveMono(BigDecimal.ONE, "amount"))
                .verifyComplete();
    }

    @Test
    void requirePositiveMono_invalid() {
        StepVerifier.create(Validators.requirePositiveMono(BigDecimal.ZERO, "amount"))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    void requireNonNegativeMono_valid() {
        StepVerifier.create(Validators.requireNonNegativeMono(BigDecimal.ZERO, "limit"))
                .verifyComplete();
    }

    @Test
    void requireNonNegativeMono_invalid() {
        StepVerifier.create(Validators.requireNonNegativeMono(BigDecimal.valueOf(-5), "limit"))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
