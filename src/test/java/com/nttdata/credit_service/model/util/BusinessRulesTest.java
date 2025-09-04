package com.nttdata.credit_service.model.util;

import com.nttdata.credit.model.CreditType;
import com.nttdata.credit_service.model.enums.CustomerType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusinessRulesTest {

    @Test
    void personal_allowsPersonalAndCard() {
        assertTrue(BusinessRules.isProductAllowed(CustomerType.PERSONAL, CreditType.PERSONAL));
        assertTrue(BusinessRules.isProductAllowed(CustomerType.PERSONAL, CreditType.CREDIT_CARD));
        assertFalse(BusinessRules.isProductAllowed(CustomerType.PERSONAL, CreditType.BUSINESS));
    }

    @Test
    void business_allowsBusinessAndCard() {
        assertTrue(BusinessRules.isProductAllowed(CustomerType.BUSINESS, CreditType.BUSINESS));
        assertTrue(BusinessRules.isProductAllowed(CustomerType.BUSINESS, CreditType.CREDIT_CARD));
        assertFalse(BusinessRules.isProductAllowed(CustomerType.BUSINESS, CreditType.PERSONAL));
    }

    @Test
    void nullValues_returnFalse() {
        assertFalse(BusinessRules.isProductAllowed(null, CreditType.PERSONAL));
        assertFalse(BusinessRules.isProductAllowed(CustomerType.PERSONAL, null));
    }
}
