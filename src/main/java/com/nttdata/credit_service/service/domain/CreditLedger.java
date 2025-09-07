//package com.nttdata.credit_service.service.domain;
//
//import com.nttdata.credit.model.CreditMovement;
//import com.nttdata.credit_service.model.CreditDocument;
//import com.nttdata.credit_service.model.CreditMovementDocument;
//import com.nttdata.credit_service.repository.CreditMovementRepository;
//import com.nttdata.credit_service.repository.CreditRepository;
//import com.nttdata.credit_service.repository.TransactionRepository;
//import com.nttdata.credit_service.service.mapper.CreditMapper;
//import com.nttdata.credit_service.service.mapper.TransactionMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.math.BigDecimal;
//
//import static com.nttdata.credit_service.model.enums.ProductType.fromCreditType;
//import static java.time.Instant.now;
//
//@Component
//@RequiredArgsConstructor
//public class CreditLedger {
//
//    private final CreditRepository creditRepo;
//    private final CreditMovementRepository movementRepo;
//    private final TransactionRepository transactionRepository;
//
//    public Mono<CreditMovement> updateBalanceAndRecord(CreditDocument doc, BigDecimal newBalance, String movementType, BigDecimal amount, String channel) {
//        doc.setBalance(newBalance);
//        doc.setUpdatedAt(now());
//
//        return creditRepo.save(doc).flatMap(saved -> {
//            var t = now();
//            var mv = new CreditMovementDocument();
//            mv.setCreditId(saved.getId());
//            mv.setType(movementType);
//            mv.setAmount(amount);
//            mv.setTxnAt(t);
//            mv.setPostedAt(t);
//            mv.setRunningBalance(newBalance);
//            mv.setChannel(channel);
//
//            return movementRepo.save(mv)
//                    .flatMap(savedMv -> {
//                        var productType = fromCreditType(saved.getType()).storageKey();
//                        var currency    = saved.getCurrency();
//                        var tx = TransactionMapper.fromMovement(savedMv, productType, currency);
//                        return transactionRepository.save(tx)
//                                .onErrorResume(org.springframework.dao.DuplicateKeyException.class, e -> Mono.empty())
//                                .thenReturn(savedMv);
//                    })
//                    .map(CreditMapper::toApi);
//        });
//    }
//}
