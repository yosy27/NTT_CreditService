package com.nttdata.credit_service.service.mapper;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.model.CreditDocument;
import com.nttdata.credit_service.model.CreditMovementDocument;
import org.springframework.stereotype.Component;

import javax.swing.plaf.nimbus.State;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;



//Utilidad de mapeo entre los modelos de la API
// y el documento de persistencia en MongoDB
@Component
public final class CreditMapper {

    private static final ZoneId UTC = ZoneOffset.UTC;

    private CreditMapper() {}

    /* ============ 1) CreditDocument -> Credit (API) ============ */
    public static Credit toApi(CreditDocument d) {
        if (d == null) return null;
        Credit api = new Credit();
        api.setId(d.getId());
        api.setCustomerId(d.getCustomerId());
        api.setType(safeEnum(CreditType.class, d.getType()));                   // String -> enum
        api.setLimit(nvl(d.getLimit(), BigDecimal.ZERO));
        api.setBalance(nvl(d.getBalance(), BigDecimal.ZERO));
        api.setInterestAnnual(nvl(d.getInterestAnnual(), BigDecimal.ZERO));
        api.setDueDate(d.getDueDate());
        api.setStatus(safeEnum(CreditStatus.class, d.getStatus()));             // String -> enum
        api.setCurrency(nvlStr(d.getCurrency(), "PEN"));
        api.setCreatedAt(toOdt(d.getCreatedAt()));
        api.setUpdatedAt(toOdt(d.getUpdatedAt()));
        api.setClosedAt(toOdt(d.getClosedAt()));
        api.setCloseReason(trimToNull(d.getCloseReason()));

        if (d.getCardId() != null || d.getCardLast4() != null || d.getCardBrand() != null) {
            Card card = new Card();
            card.setId(trimToNull(d.getCardId()));
            card.setLast4(trimToNull(d.getCardLast4()));
            card.setBrand(trimToNull(d.getCardBrand()));
            api.setCard(card);
        }
        return api;
    }

    /* ============ 2) CreditCreate (+customerId) -> CreditDocument ============ */
    public static CreditDocument fromCreate(CreditCreate req, String customerId) {
        if (req == null) return null;
        CreditDocument e = new CreditDocument();
        e.setCustomerId(customerId);                                            // ¡ya resuelto!
        e.setType(asString(req.getType()));                                     // enum -> String
        e.setLimit(nvl(req.getLimit(), BigDecimal.ZERO));
        e.setBalance(BigDecimal.ZERO);                                          // saldo inicial
        e.setInterestAnnual(nvl(req.getInterestAnnual(), BigDecimal.ZERO));
        e.setDueDate(req.getDueDate());
        e.setStatus(asString(CreditStatus.ACTIVE));                              // estado inicial
        e.setCurrency(nvlStr(req.getCurrency(), "PEN"));
        Instant now = Instant.now();
        e.setCreatedAt(now);
        e.setUpdatedAt(now);

        if (req.getCard() != null) {
            e.setCardId(trimToNull(req.getCard().getId()));
            e.setCardLast4(trimToNull(req.getCard().getLast4()));
            e.setCardBrand(trimToNull(req.getCard().getBrand()));
        }
        return e;
    }

    /* ============ 3) merge PATCH: (doc + update) -> doc ============ */
    public static void merge(CreditDocument target, CreditUpdate patch) {
        if (target == null || patch == null) return;
        if (patch.getInterestAnnual() != null) target.setInterestAnnual(patch.getInterestAnnual());
        if (patch.getDueDate() != null) target.setDueDate(patch.getDueDate());
        if (patch.getCard() != null) {
            if (patch.getCard().getId() != null) target.setCardId(trimToNull(patch.getCard().getId()));
            if (patch.getCard().getLast4() != null) target.setCardLast4(trimToNull(patch.getCard().getLast4()));
            if (patch.getCard().getBrand() != null) target.setCardBrand(trimToNull(patch.getCard().getBrand()));
        }
        target.setUpdatedAt(Instant.now());
    }

    /* ============ 4) CreditDocument -> CreditBalance (API) ============ */
    public static CreditBalance toApiBalance(CreditDocument d) {
        if (d == null) return null;
        BigDecimal limit = nvl(d.getLimit(), BigDecimal.ZERO);
        BigDecimal bal = nvl(d.getBalance(), BigDecimal.ZERO);
        CreditBalance b = new CreditBalance();
        b.setLimit(limit);
        b.setBalance(bal);
        b.setAvailable(limit.subtract(bal).max(BigDecimal.ZERO));
        return b;
    }

    /* ============ 5) CreditMovementDocument -> CreditMovement (API) ============ */
    public static CreditMovement toApi(CreditMovementDocument m) {
        if (m == null) return null;
        CreditMovement api = new CreditMovement();
        api.setId(m.getId());
        api.setCreditId(m.getCreditId());
        // En tus modelos generados, Movement type suele ser inner enum:
        // enum: [CHARGE, PAYMENT, INTEREST, FEE, REVERSAL]
        api.setType(safeEnum(CreditMovement.TypeEnum.class, m.getType()));      // String -> enum
        api.setAmount(nvl(m.getAmount(), BigDecimal.ZERO));
        api.setTxnAt(toOdt(m.getTxnAt()));
        api.setPostedAt(toOdt(m.getPostedAt()));
        api.setRunningBalance(nvl(m.getRunningBalance(), BigDecimal.ZERO));
        api.setChannel(trimToNull(m.getChannel()));
        return api;
    }

    /* ======================= helpers ======================= */

    private static BigDecimal nvl(BigDecimal v, BigDecimal def) { return v == null ? def : v; }

    private static String trimToNull(String v) {
        if (v == null) return null;
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    private static String nvlStr(String v, String def) {
        String t = trimToNull(v);
        return t == null ? def : t;
    }

    private static OffsetDateTime toOdt(Instant i) {
        return i == null ? null : OffsetDateTime.ofInstant(i, UTC);
    }

    private static <E extends Enum<E>> E safeEnum(Class<E> type, String value) {
        if (value == null) return null;
        try { return Enum.valueOf(type, value); }
        catch (IllegalArgumentException ex) { return null; }
    }

    // Si tus modelos generados tienen getValue()/fromValue():
    private static String asString(Enum<?> e) { return e == null ? null : e.name(); }
}
