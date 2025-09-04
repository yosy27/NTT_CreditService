package com.nttdata.credit_service.model.util;

import java.math.BigDecimal;

public final class CreditUtils {

    private CreditUtils() {}

    public  static BigDecimal nvl(BigDecimal v, BigDecimal def) { return v == null ? def : v; }

    public  static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        try { return Enum.valueOf(type, value); } catch (Exception e) { return null; }
    }

    public  static String enumName(Enum<?> e) {
        return e == null ? null : e.name();
    }


}
