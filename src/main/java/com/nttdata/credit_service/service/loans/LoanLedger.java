package com.nttdata.credit_service.service.loans;

import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit_service.domain.LoanDocument;
import com.nttdata.credit_service.repository.LoanRepository;
import com.nttdata.credit_service.service.common.LedgerCore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

/// Orquestador de balance + registro de movimiento para PRÉSTAMOS.
@Component
@RequiredArgsConstructor
public class LoanLedger {
    private final LoanRepository loanRepo;
    private final LedgerCore core;

    //Actualiza el saldo del préstamo y registra un movimiento asociado
    public Mono<CreditMovement> updateBalanceAndRecord(LoanDocument doc, BigDecimal newBalance, String movementType, BigDecimal amount, String channel) {
        // Actualiza saldo del préstamo
        doc.setBalance(newBalance);
        doc.setUpdatedAt(Instant.now());
        // Persiste y registra el movimiento en el ledger/transacciones
        return loanRepo.save(doc)
                .flatMap(saved -> core.record(saved.getId(), movementType, amount, newBalance, channel, "LOAN", saved.getCurrency()));
    }
}
