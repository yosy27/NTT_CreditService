package com.nttdata.credit_service.service.mapper;

import com.nttdata.credit.model.CreditBalance;
import com.nttdata.credit.model.CreditMovement;
import com.nttdata.credit.model.CreditProductSummary;
import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit_service.domain.CreditLineDocument;
import com.nttdata.credit_service.domain.LoanDocument;
import com.nttdata.credit_service.domain.MovementDocument;

import java.math.BigDecimal;

import static com.nttdata.credit_service.shared.util.MapperUtils.toOdt;
import static com.nttdata.credit_service.shared.util.Numbers.nz;

public final class CreditProductMapper {

    private CreditProductMapper() {}

    //Construye un resumen de producto de crédito para un Loan.
    public static CreditProductSummary toSummary(LoanDocument d) {
        var s = new CreditProductSummary();
        s.setId(d.getId());
        s.setKind(CreditProductSummary.KindEnum.LOAN);
        s.setStatus(CreditStatus.fromValue(d.getStatus()));
        s.setLimit(nz(d.getLimit()));
        s.setBalance(nz(d.getBalance()));
        s.setCurrency(d.getCurrency());
        s.setCreatedAt(toOdt(d.getCreatedAt()));
        return s;
    }

    //Construye un resumen de producto de crédito para una Línea de Crédito.
    public static CreditProductSummary toSummary(CreditLineDocument d) {
        var s = new CreditProductSummary();
        s.setId(d.getId());
        s.setKind(CreditProductSummary.KindEnum.CREDIT_LINE);
        s.setStatus(CreditStatus.fromValue(d.getStatus()));
        s.setLimit(nz(d.getLimit()));
        s.setBalance(nz(d.getBalance()));
        s.setCurrency(d.getCurrency());
        s.setCreatedAt(toOdt(d.getCreatedAt()));
        return s;
    }

    //Calcula el balance del producto.
    public static CreditBalance toBalance(BigDecimal limit, BigDecimal balance) {
        var b = new CreditBalance();
        b.setLimit(limit);
        b.setBalance(balance);
        b.setAvailable(limit.subtract(balance));
        return b;
    }

    //Convierte un movimiento de dominio a modelo API.
    public static CreditMovement toMovement(MovementDocument mv) {
        var m = new CreditMovement();
        m.setId(mv.getId());
        m.setCreditId(mv.getCreditId());
        m.setType(CreditMovement.TypeEnum.fromValue(mv.getType()));
        m.setAmount(nz(mv.getAmount()));
        m.setTxnAt(toOdt(mv.getTxnAt()));
        m.setPostedAt(toOdt(mv.getPostedAt()));
        m.setRunningBalance(nz(mv.getRunningBalance()));
        m.setChannel(mv.getChannel());
        return m;
    }
}
