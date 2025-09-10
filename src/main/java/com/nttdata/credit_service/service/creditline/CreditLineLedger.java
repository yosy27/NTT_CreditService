package com.nttdata.credit_service.service.creditline;

import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit_service.domain.CreditLineDocument;
import com.nttdata.credit_service.repository.CreditLineRepository;
import com.nttdata.credit_service.service.common.LedgerCore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

/// Orquestador de balance + registro de movimiento para PRÉSTAMOS.
@Component
@RequiredArgsConstructor
public class CreditLineLedger {
    private final CreditLineRepository repo;
    private final LedgerCore core;

    public Mono<CreditMovement> updateBalanceAndRecord(CreditLineDocument doc, BigDecimal newBalance, String movementType, BigDecimal amount, String channel) {
        doc.setBalance(newBalance);
        doc.setUpdatedAt(Instant.now());
        return repo.save(doc)
                .flatMap(saved -> core.record(saved.getId(), movementType, amount, newBalance, channel, "CREDIT_LINE", saved.getCurrency()));
    }
}
