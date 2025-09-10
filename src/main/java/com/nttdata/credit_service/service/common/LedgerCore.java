package com.nttdata.credit_service.service.common;


import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit_service.domain.MovementDocument;
import com.nttdata.credit_service.repository.MovementRepository;
import com.nttdata.credit_service.repository.TransactionRepository;
import com.nttdata.credit_service.service.mapper.CreditProductMapper;
import com.nttdata.credit_service.service.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

@RequiredArgsConstructor
public class LedgerCore {
    private final MovementRepository movementRepo;
    private final TransactionRepository txRepo;

    public Mono<CreditMovement> record(String creditId,
                                       String movementType,
                                       BigDecimal amount,
                                       BigDecimal newBalance,
                                       String channel,
                                       String productKind,
                                       String currency) {
        var now = Instant.now();
        var mv = new MovementDocument();
        mv.setCreditId(creditId);
        mv.setType(movementType);
        mv.setAmount(amount);
        mv.setTxnAt(now);
        mv.setPostedAt(now);
        mv.setRunningBalance(newBalance);
        mv.setChannel(channel);
        mv.setProductKind(productKind);

        return movementRepo.save(mv)
                .flatMap(savedMv -> txRepo.save(
                                TransactionMapper.fromMovement(savedMv, productKind, currency))
                        .onErrorResume(org.springframework.dao.DuplicateKeyException.class, e -> Mono.empty())
                        .thenReturn(savedMv))
                .map(CreditProductMapper::toMovement);
    }
}
