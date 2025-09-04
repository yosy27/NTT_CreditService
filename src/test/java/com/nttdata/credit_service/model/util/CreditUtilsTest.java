package com.nttdata.credit_service.model.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CreditUtilsTest {

    enum Dummy { A, B }

    @Test
    void nvl_returnsDefaultWhenNull() {
        assertEquals(BigDecimal.TEN, CreditUtils.nvl(null, BigDecimal.TEN));
    }

    @Test
    void nvl_returnsValueWhenNotNull() {
        assertEquals(BigDecimal.ONE, CreditUtils.nvl(BigDecimal.ONE, BigDecimal.TEN));
    }

    @Test
    void enumName_handlesNullAndValid() {
        assertNull(CreditUtils.enumName(null));
        assertEquals("A", CreditUtils.enumName(Dummy.A));
    }

    @Test
    void parseEnum_validAndInvalid() {
        assertEquals(Dummy.B, CreditUtils.parseEnum(Dummy.class, "B"));
        assertNull(CreditUtils.parseEnum(Dummy.class, "X"));
    }
}
