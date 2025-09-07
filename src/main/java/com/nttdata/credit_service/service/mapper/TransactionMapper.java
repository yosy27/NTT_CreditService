//package com.nttdata.credit_service.service.mapper;
//
//import com.nttdata.credit_service.model.CreditMovementDocument;
//import com.nttdata.credit_service.model.transaction.TransactionDocument;
//
//import java.time.Instant;
//import java.util.Locale;
//
//public class TransactionMapper {
//
//    public static TransactionDocument fromMovement(CreditMovementDocument mv,  String productoType, String currency) {
//
//        String txnType = normalizeTxnType(mv.getType());
//
//        return TransactionDocument.builder()
//                .movementId(mv.getId())
//                .creditId(mv.getCreditId())
//                .productType(productoType)                  // mapear según tipo de crédito
//                .type(txnType)
//                .amount(mv.getAmount())
//                .currency(currency)
//                .createdDate(mv.getTxnAt() != null ? mv.getTxnAt() : Instant.now())
//                .build();
//    }
//
//    private static String normalizeTxnType(String movementType) {
//        if (movementType == null) return "CHARGE";
//        return "PAYMENT".equalsIgnoreCase(movementType) ? "PAYMENT" : "CHARGE";
//    }
//}
