package com.nttdata.credit_service.service.policy;

import com.nttdata.credit.model.LoanType;
import com.nttdata.credit_service.domain.enums.CustomerType;

// Reglas de negocio relacionadas a la admisión de préstamos.
public class BusinessRules {
    private BusinessRules() {}

    //Determina si un cliente puede acceder a un tipo de préstamo.
    public static boolean isLoanAllowed(CustomerType customer, LoanType loan) {
        if (customer == null || loan == null) return false;
        switch (customer) {
            case PERSONAL: return loan == LoanType.PERSONAL;
            case BUSINESS: return loan == LoanType.BUSINESS;
            default: return false;
        }
    }

}
