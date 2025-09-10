package com.nttdata.credit_service.shared.util;

import java.math.BigDecimal;

//Clase utilitaria para manejo de números y enums.
public final class Numbers {

    private Numbers() {}

    //Retorna el valor dado o un valor por defecto si es nulo.
    public  static BigDecimal nvl(BigDecimal v, BigDecimal def) {
        return v == null ? def : v;
    }

    // Convierte un String en un Enum del tipo indicado
    public  static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        try {
            return Enum.valueOf(type, value);
        } catch (Exception e) {
            return null;
        }
    }

    //Retorna el valor dado o BigDecimal.ZERO si es nulo.
    public static BigDecimal nz(BigDecimal v) {
        return nvl(v, BigDecimal.ZERO);
    }


    //Retorna el nombre de un Enum
    public  static String enumName(Enum<?> e) {
        return e == null ? null : e.name();
    }


}
