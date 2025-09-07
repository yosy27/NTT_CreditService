//package com.nttdata.credit_service.service.domain;
//
//import com.nttdata.credit.model.CreditMovement;
//import com.nttdata.credit_service.model.CreditDocument;
//import com.nttdata.credit_service.model.CreditMovementDocument;
//import com.nttdata.credit_service.repository.CreditMovementRepository;
//import com.nttdata.credit_service.repository.CreditRepository;
//import com.nttdata.credit_service.repository.TransactionRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class CreditLedgerTest {
//
//    @Test
//    void updateBalanceAndRecord_updatesDoc_createsMovement_andTransaction() {
//        CreditRepository creditRepo = mock(CreditRepository.class);
//        CreditMovementRepository movementRepo = mock(CreditMovementRepository.class);
//        TransactionRepository txRepo = mock(TransactionRepository.class);
//
//        CreditLedger ledger = new CreditLedger(creditRepo, movementRepo, txRepo);
//
//        var doc = new CreditDocument();
//        doc.setId("C1");
//        doc.setType("PERSONAL");
//        doc.setCurrency("PEN");
//        doc.setBalance(BigDecimal.ZERO);
//
//        when(creditRepo.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
//        when(movementRepo.save(any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
//        when(txRepo.save(any())).thenReturn(Mono.empty());
//
//        Mono<CreditMovement> mono = ledger.updateBalanceAndRecord(
//                doc, BigDecimal.valueOf(50), "CHARGE", BigDecimal.valueOf(50), "CARD"
//        );
//
//        StepVerifier.create(mono)
//                .expectNextMatches(mv ->
//                        "C1".equals(mv.getCreditId())
//                                && mv.getAmount().compareTo(BigDecimal.valueOf(50)) == 0
//                                && CreditMovement.TypeEnum.CHARGE.equals(mv.getType())
//                )
//                .verifyComplete();
//
//        var captor = ArgumentCaptor.forClass(CreditDocument.class);
//        verify(creditRepo, times(1)).save(captor.capture());
//        assertEquals(0, captor.getValue().getBalance().compareTo(BigDecimal.valueOf(50)));
//
//        verify(movementRepo, times(1)).save(any(CreditMovementDocument.class));
//        verify(txRepo, times(1)).save(any());
//    }
//}
