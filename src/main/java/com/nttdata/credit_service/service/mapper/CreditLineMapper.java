package com.nttdata.credit_service.service.mapper;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.domain.CreditLineDocument;
import com.nttdata.credit_service.domain.MovementDocument;

import java.math.BigDecimal;

import static com.nttdata.credit_service.shared.util.MapperUtils.toOdt;
import static java.util.Objects.nonNull;

public class CreditLineMapper {

    private CreditLineMapper() {}

    // ==== Create ====
    //Construye un documento de línea de crédito desde la solicitud.
    public static CreditLineDocument fromCreate(CreditLineRequest r) {
        CreditLineDocument d = new CreditLineDocument();
        d.setCustomerId(r.getCustomerId());
        d.setLimit(r.getLimit());
        d.setBalance(BigDecimal.ZERO);
        d.setInterestAnnual(r.getInterestAnnual());
        d.setBillingCycleDay(r.getBillingCycleDay());
        d.setPaymentDueDay(r.getPaymentDueDay());
        d.setCurrency(nonNull(r.getCurrency()) ? r.getCurrency() : "PEN");
        d.setStatus(CreditStatus.ACTIVE.name());
        return d;
    }

    // ==== Read ====
    //Convierte un documento de línea de crédito al modelo de respuesta API
    public static CreditLineResponse toApi(CreditLineDocument d) {
        CreditLineResponse r = new CreditLineResponse();
        r.setId(d.getId());
        r.setCustomerId(d.getCustomerId());
        r.setLimit(d.getLimit());
        r.setBalance(d.getBalance());
        r.setInterestAnnual(d.getInterestAnnual());
        r.setBillingCycleDay(d.getBillingCycleDay());
        r.setPaymentDueDay(d.getPaymentDueDay());
        r.setStatus(CreditStatus.fromValue(d.getStatus()));
        r.setCurrency(d.getCurrency());
        r.setClosedAt(toOdt(d.getClosedAt()));
        r.setCloseReason(d.getCloseReason());
        r.setCreatedAt(toOdt(d.getCreatedAt()));
        r.setUpdatedAt(toOdt(d.getUpdatedAt()));
        return r;
    }

    // ==== Patch (campos permitidos) ====
    // Aplica cambios parciales permitidos:
    public static CreditLineDocument merge(CreditLineDocument d, CreditLineUpdate p) {
        if (p == null) return d;
        if (p.getInterestAnnual() != null) d.setInterestAnnual(p.getInterestAnnual());
        if (p.getBillingCycleDay() != null) d.setBillingCycleDay(p.getBillingCycleDay());
        if (p.getPaymentDueDay() != null)   d.setPaymentDueDay(p.getPaymentDueDay());
        return d;
    }

    // ==== Put (campos editables) ====
    // Aplica edición completa de campos editables.
    public static void mergeAll(CreditLineDocument d, CreditLineRequest r) {
        if (r == null) return;
        if (r.getLimit() != null)          d.setLimit(r.getLimit());
        if (r.getInterestAnnual() != null) d.setInterestAnnual(r.getInterestAnnual());
        if (r.getBillingCycleDay() != null) d.setBillingCycleDay(r.getBillingCycleDay());
        if (r.getPaymentDueDay() != null)   d.setPaymentDueDay(r.getPaymentDueDay());
        if (r.getCurrency() != null)       d.setCurrency(r.getCurrency());
    }

}
