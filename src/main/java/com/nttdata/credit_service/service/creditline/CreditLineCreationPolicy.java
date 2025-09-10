package com.nttdata.credit_service.service.creditline;

import com.nttdata.credit_service.service.common.CommonAdmissionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/// Reglas de admisión para creación de LÍNEAS de crédito.
@Component
@RequiredArgsConstructor
public class CreditLineCreationPolicy {
    private final CommonAdmissionPolicy common;

    public Mono<Void> validate(String customerId) {
        return common.ensureCustomerAdmissible(customerId);
    }
}
