//package com.nttdata.credit_service.model.util;
//
//import com.nttdata.credit.model.CreditType;
//import com.nttdata.credit_service.model.enums.CustomerType;
//
//public final class BusinessRules {
//
//    private BusinessRules() {}
//
//    public static boolean isProductAllowed(CustomerType customerType, CreditType productType) {
//        if (customerType == null || productType == null) return false;
//        switch (customerType) {
//            case PERSONAL:
//                return productType == CreditType.PERSONAL || productType == CreditType.CREDIT_CARD;
//            case BUSINESS:
//                return productType == CreditType.BUSINESS  || productType == CreditType.CREDIT_CARD;
//            default:
//                return false;
//        }
//    }
//}
