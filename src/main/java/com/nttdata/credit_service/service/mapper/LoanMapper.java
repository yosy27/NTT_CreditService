package com.nttdata.credit_service.service.mapper;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.domain.MovementDocument;
import com.nttdata.credit_service.domain.LoanDocument;

import java.math.BigDecimal;

import static com.nttdata.credit_service.shared.util.MapperUtils.toOdt;
import static java.util.Objects.nonNull;

//   Mapeos entre LoanDocument
public class LoanMapper {
    private LoanMapper() {}

    // ==== Create ====
    //Construye un LoanDocument a partir de la solicitud de creación.
    public static LoanDocument fromCreate(LoanRequest req, String customerId) {
        LoanDocument d = new LoanDocument();
        d.setCustomerId(customerId);
        d.setType(req.getType() != null ? req.getType().getValue() : null); // o .name()
        d.setLimit(req.getLimit());
        d.setBalance(BigDecimal.ZERO);
        d.setInterestAnnual(req.getInterestAnnual());
        d.setDueDate(req.getDueDate());
        d.setCurrency(nonNull(req.getCurrency()) ? req.getCurrency() : "PEN");
        d.setStatus(CreditStatus.ACTIVE.name());
        return d;
    }

    // ==== Read ====
    // Convierte un LoanDocument a su representación API
    // Usa conversiones seguras para enums
    public static LoanResponse toApi(LoanDocument d) {
        LoanResponse r = new LoanResponse();
        r.setId(d.getId());
        r.setCustomerId(d.getCustomerId());
        r.setType(LoanType.fromValue(d.getType())); // o LoanType.valueOf(d.getType())
        r.setLimit(d.getLimit());
        r.setBalance(d.getBalance());
        r.setInterestAnnual(d.getInterestAnnual());
        r.setDueDate(d.getDueDate());
        r.setStatus(CreditStatus.fromValue(d.getStatus())); // o CreditStatus.valueOf(...)
        r.setCurrency(d.getCurrency());
        r.setClosedAt(toOdt(d.getClosedAt()));
        r.setCloseReason(d.getCloseReason());
        r.setCreatedAt(toOdt(d.getCreatedAt()));
        r.setUpdatedAt(toOdt(d.getUpdatedAt()));
        return r;
    }


    // ==== Patch (campos permitidos) ====
    // Aplica cambios parciales permitidos (interestAnnual, dueDate)
    public static LoanDocument merge(LoanDocument d, LoanUpdate p) {
        if (p == null) return d;
        if (p.getInterestAnnual() != null) d.setInterestAnnual(p.getInterestAnnual());
        if (p.getDueDate() != null)        d.setDueDate(p.getDueDate());
        return d;
    }

    // ==== Put (campos editables) ====
    public static void mergeAll(LoanDocument d, LoanRequest r) {
        if (r == null) return;
        if (r.getLimit() != null)          d.setLimit(r.getLimit());
        if (r.getInterestAnnual() != null) d.setInterestAnnual(r.getInterestAnnual());
        if (r.getDueDate() != null)        d.setDueDate(r.getDueDate());
        if (r.getCurrency() != null)       d.setCurrency(r.getCurrency());
        // NO editar: id, customerId, type, status
    }

}
