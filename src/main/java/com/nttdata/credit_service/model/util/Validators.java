package com.nttdata.credit_service.model.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public final class Validators {
    private Validators() {}

    public static Mono<Void> requirePositiveMono(BigDecimal value, String field) {
        return (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
                ? Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be > 0"))
                : Mono.empty();
    }

    public static Mono<Void> requireNonNegativeMono(BigDecimal value, String field) {
        return (value == null || value.compareTo(BigDecimal.ZERO) < 0)
                ? Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be >= 0"))
                : Mono.empty();
    }

}

