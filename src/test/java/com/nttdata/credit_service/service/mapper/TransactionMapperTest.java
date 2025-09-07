//package com.nttdata.credit_service.service.mapper;
//
//import com.nttdata.credit_service.model.CreditMovementDocument;
//import com.nttdata.credit_service.model.transaction.TransactionDocument;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TransactionMapperTest {
//
//    @Test
//    void fromMovement_mapsFields_andNormalizesType_caseInsensitive() {
//        CreditMovementDocument mv = new CreditMovementDocument();
//        mv.setId("m-1");
//        mv.setCreditId("c-1");
//        mv.setType("payment"); // minúsculas
//        mv.setAmount(new BigDecimal("50.00"));
//        mv.setTxnAt(Instant.ofEpochSecond(123));
//
//        TransactionDocument tx = TransactionMapper.fromMovement(mv, "CREDIT_CARD", "USD");
//        assertNotNull(tx);
//        assertEquals("m-1", tx.getMovementId());
//        assertEquals("c-1", tx.getCreditId());
//        assertEquals("CREDIT_CARD", tx.getProductType());
//        assertEquals("PAYMENT", tx.getType());              // normalizado
//        assertEquals(new BigDecimal("50.00"), tx.getAmount());
//        assertEquals("USD", tx.getCurrency());
//        assertEquals(Instant.ofEpochSecond(123), tx.getCreatedDate());
//    }
//
//    @Test
//    void fromMovement_defaultsTypeToCharge_whenNullOrUnknown_andSetsCreatedNowIfTxnAtNull() {
//        CreditMovementDocument mv = new CreditMovementDocument();
//        mv.setId("m-2");
//        mv.setCreditId("c-2");
//        mv.setType(null); // → CHARGE
//        mv.setAmount(new BigDecimal("10.00"));
//        mv.setTxnAt(null); // createdDate se setea a now
//
//        Instant t0 = Instant.now();
//        TransactionDocument tx = TransactionMapper.fromMovement(mv, "PERSONAL", "PEN");
//        Instant t1 = Instant.now();
//
//        assertEquals("CHARGE", tx.getType());
//        assertEquals("PERSONAL", tx.getProductType());
//        assertEquals("PEN", tx.getCurrency());
//        assertTrue(!tx.getCreatedDate().isBefore(t0) && !tx.getCreatedDate().isAfter(t1),
//                "createdDate debe estar entre t0 y t1 cuando txnAt es null");
//    }
//
//    @Test
//    void fromMovement_unknownType_becomesCharge() {
//        CreditMovementDocument mv = new CreditMovementDocument();
//        mv.setType("INTEREST"); // cualquier valor distinto a PAYMENT -> CHARGE
//        TransactionDocument tx = TransactionMapper.fromMovement(mv, "BUSINESS", "USD");
//        assertEquals("CHARGE", tx.getType());
//    }
//}
