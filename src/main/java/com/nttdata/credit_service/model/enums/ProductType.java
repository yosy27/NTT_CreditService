package com.nttdata.credit_service.model.enums;

public enum ProductType {
    PERSONAL_CREDIT, BUSINESS_CREDIT, CREDIT_CARD;

    public static ProductType fromCreditType(String creditType) {
        if (creditType == null) return PERSONAL_CREDIT;
        switch (creditType.trim().toUpperCase()) {
            case "BUSINESS":
                return BUSINESS_CREDIT;
            case "CREDIT_CARD":
                return CREDIT_CARD;
            default:
                return PERSONAL_CREDIT;
        }
    }

    public String storageKey() {
        return name().toLowerCase();
    }
}
