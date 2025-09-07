//package com.nttdata.credit_service.service.mapper;
//
//import com.nttdata.credit.model.*;
//import com.nttdata.credit_service.model.CreditDocument;
//import com.nttdata.credit_service.model.CreditMovementDocument;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//import static com.nttdata.credit_service.model.util.CreditUtils.parseEnum;
//import static com.nttdata.credit_service.service.mapper.util.MapperUtils.*;
//
//
////Utilidad de mapeo entre los modelos de la API
//// y el documento de persistencia en MongoDB
//public final class CreditMapper {
//
//    private CreditMapper() {}
//
//    /* =================== 1) CreditDocument -> CreditResponse =================== */
//    public static CreditResponse toApi(CreditDocument d) {
//        if (d == null) return null;
//
//        CreditResponse api = new CreditResponse();
//        api.setId(d.getId());
//        api.setCustomerId(d.getCustomerId());
//        api.setType(parseEnum(CreditType.class, d.getType()));
//        api.setLimit(orZero(d.getLimit()));
//        api.setBalance(orZero(d.getBalance()));
//        api.setInterestAnnual(orZero(d.getInterestAnnual()));
//        api.setDueDate(d.getDueDate());
//        api.setStatus(parseEnum(CreditStatus.class, d.getStatus()));
//        api.setCurrency(orDefault(d.getCurrency(), "PEN"));
//        api.setCreatedAt(toOdt(d.getCreatedAt()));
//        api.setUpdatedAt(toOdt(d.getUpdatedAt()));
//        api.setClosedAt(toOdt(d.getClosedAt()));
//        api.setCloseReason(normalize(d.getCloseReason()));
//
//        if (hasCard(d)) api.setCard(toApiCard(d));
//        return api;
//    }
//
//    /* =================== 2) CreditRequest (+customerId) -> CreditDocument =================== */
//    public static CreditDocument fromCreate(CreditRequest req, String customerId) {
//        if (req == null) return null;
//
//        Instant now = Instant.now();
//
//        CreditDocument e = new CreditDocument();
//        e.setCustomerId(customerId);
//        e.setType(asString(req.getType()));
//        e.setLimit(orZero(req.getLimit()));
//        e.setBalance(BigDecimal.ZERO);
//        e.setInterestAnnual(orZero(req.getInterestAnnual()));
//        e.setDueDate(req.getDueDate());
//        e.setStatus(asString(CreditStatus.ACTIVE));
//        e.setCurrency(orDefault(req.getCurrency(), "PEN"));
//        e.setCreatedAt(now);
//        e.setUpdatedAt(now);
//
//        applyCardFromApi(e, req.getCard());
//        return e;
//    }
//
//    /* =================== 3) PATCH merge: (doc + update) -> doc =================== */
//    public static CreditDocument merge(CreditDocument target, CreditUpdate patch) {
//        if (target == null || patch == null) return target;
//
//        if (patch.getInterestAnnual() != null) target.setInterestAnnual(patch.getInterestAnnual());
//        if (patch.getDueDate() != null)        target.setDueDate(patch.getDueDate());
//        if (patch.getCard() != null)           applyCardFromApi(target, patch.getCard());
//
//        touchUpdated(target);
//        return target;
//    }
//
//    /* =================== 4) CreditDocument -> CreditBalance =================== */
//    public static CreditBalance toApiBalance(CreditDocument d) {
//        if (d == null) return null;
//
//        BigDecimal limit = orZero(d.getLimit());
//        BigDecimal bal   = orZero(d.getBalance());
//
//        CreditBalance b = new CreditBalance();
//        b.setLimit(limit);
//        b.setBalance(bal);
//        b.setAvailable(limit.subtract(bal).max(BigDecimal.ZERO));
//        return b;
//    }
//
//    /* =================== 5) CreditMovementDocument -> CreditMovement =================== */
//    public static CreditMovement toApi(CreditMovementDocument m) {
//        if (m == null) return null;
//
//        CreditMovement api = new CreditMovement();
//        api.setId(m.getId());
//        api.setCreditId(m.getCreditId());
//        api.setType(parseEnum(CreditMovement.TypeEnum.class, m.getType()));
//        api.setAmount(orZero(m.getAmount()));
//        api.setTxnAt(toOdt(m.getTxnAt()));
//        api.setPostedAt(toOdt(m.getPostedAt()));
//        api.setRunningBalance(orZero(m.getRunningBalance()));
//        api.setChannel(normalize(m.getChannel()));
//        return api;
//    }
//
//    /* =================== 6) mergeAll: (doc + request completo) -> doc =================== */
//    public static CreditDocument mergeAll(CreditDocument target, CreditRequest src) {
//        if (target == null || src == null) return target;
//
//        if (src.getType() != null)           target.setType(asString(src.getType()));
//        if (src.getLimit() != null)          target.setLimit(src.getLimit());
//        if (src.getInterestAnnual() != null) target.setInterestAnnual(src.getInterestAnnual());
//        if (src.getDueDate() != null)        target.setDueDate(src.getDueDate());
//        if (src.getCurrency() != null)       target.setCurrency(orDefault(src.getCurrency(), target.getCurrency()));
//
//        applyCardFromApi(target, src.getCard());
//        touchUpdated(target);
//        return target;
//    }
//
//}
