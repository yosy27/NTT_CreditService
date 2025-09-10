package com.nttdata.credit_service.shared.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

// Clase utilitaria que centraliza validaciones
public final class Validators {
    private Validators() {}

    //Valida que el valor sea estrictamente mayor a cero
    public static Mono<Void> requirePositiveMono(BigDecimal value, String field) {
        return (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
                ? Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be > 0"))
                : Mono.empty();
    }

    //Valida que el valor sea mayor o igual a cero
    public static Mono<Void> requireNonNegativeMono(BigDecimal value, String field) {
        return (value == null || value.compareTo(BigDecimal.ZERO) < 0)
                ? Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be >= 0"))
                : Mono.empty();
    }

}
