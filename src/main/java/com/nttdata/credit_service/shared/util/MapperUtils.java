package com.nttdata.credit_service.shared.util;

import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

//Clase utilitaria para conversiones comunes de tiempo.
public class MapperUtils {
    public MapperUtils() {}

    //Convierte un Instant a OffsetDateTime en UTC.
    public static OffsetDateTime toOdt(Instant i) {
        return i == null ? null : OffsetDateTime.ofInstant(i, UTC);
    }

}
