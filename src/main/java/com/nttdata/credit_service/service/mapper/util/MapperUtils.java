package com.nttdata.credit_service.service.mapper.util;

import com.nttdata.credit.model.Card;
import com.nttdata.credit_service.model.CreditDocument;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

public final class MapperUtils {

    public MapperUtils() {}

    /* ===== Nombres nuevos que usas en CreditMapper ===== */
    public static BigDecimal orZero(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    public static String normalize(String v) {
        if (v == null) return null;
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    public static String orDefault(String v, String def) {
        String t = normalize(v);
        return t == null ? def : t;
    }

    public static OffsetDateTime toOdt(Instant i) {
        return i == null ? null : OffsetDateTime.ofInstant(i, UTC);
    }

    public static <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        if (value == null) return null;
        try { return Enum.valueOf(type, value); }
        catch (IllegalArgumentException ex) { return null; }
    }

    public static String asString(Enum<?> e) {
        return e == null ? null : e.name();
    }

    /* ===== (Opcionales) aliases de tus nombres viejos ===== */
    public static BigDecimal nvl(BigDecimal v, BigDecimal def) { return v == null ? def : v; }
    public static String trimToNull(String v) { return normalize(v); }
    public static String nvlStr(String v, String def) { return orDefault(v, def); }
    public static <E extends Enum<E>> E safeEnum(Class<E> type, String value) { return parseEnum(type, value); }

    public static boolean hasCard(CreditDocument d) {
        return d.getCardId() != null || d.getCardLast4() != null || d.getCardBrand() != null;
    }

    public static Card toApiCard(CreditDocument d) {
        Card card = new Card();
        card.setId(normalize(d.getCardId()));
        card.setLast4(normalize(d.getCardLast4()));
        card.setBrand(normalize(d.getCardBrand()));
        return card;
    }

    public static void applyCardFromApi(CreditDocument target, Card card) {
        if (target == null || card == null) return;
        if (card.getId() != null)     target.setCardId(normalize(card.getId()));
        if (card.getLast4() != null)  target.setCardLast4(normalize(card.getLast4()));
        if (card.getBrand() != null)  target.setCardBrand(normalize(card.getBrand()));
    }

    public static void touchUpdated(CreditDocument target) {
        if (target != null) target.setUpdatedAt(Instant.now());
    }
}
