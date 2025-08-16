package com.nttdata.credit_service.service;

import com.nttdata.credit.model.*;
import com.nttdata.credit_service.persistence.CreditDocument;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 *  * Utilidad de mapeo entre los modelos de la API (requests/responses)
 *  * y el documento de persistencia en MongoDB.
 */
public final class CreditMapper {
    private CreditMapper(){}


    public static CreditDocument toDoc(CreditRequest r){
        CreditDocument d = new CreditDocument();
        d.setCustomerId(r.getCustomerId());
        d.setType(r.getType()); // enum directo
        d.setLimit(r.getLimit());
        d.setBalance(0.0);
        d.setInterestAnnual(r.getInterestAnnual());
        d.setDueDate(r.getDueDate() != null ? r.getDueDate().toString() : null); // LocalDate -> String (ISO)
        if (r.getCard() != null) {
            d.setCardId(r.getCard().getId());
            d.setCardLast4(r.getCard().getLast4());
            d.setCardBrand(r.getCard().getBrand());
        }
        d.setStatus(CreditStatus.ACTIVE); // enum directo
        d.setCreatedAt(OffsetDateTime.now().toInstant());
        d.setUpdatedAt(OffsetDateTime.now().toInstant());
        return d;
    }

    public static void applyUpdate(CreditDocument d, CreditUpdateRequest r){
        d.setCustomerId(r.getCustomerId());
        d.setType(r.getType()); // enum directo
        d.setLimit(r.getLimit());
        d.setBalance(r.getBalance());
        d.setInterestAnnual(r.getInterestAnnual());
        d.setDueDate(r.getDueDate() != null ? r.getDueDate().toString() : null); // LocalDate -> String
        d.setStatus(r.getStatus()); // enum directo
        if (r.getCard() != null) {
            d.setCardId(r.getCard().getId());
            d.setCardLast4(r.getCard().getLast4());
            d.setCardBrand(r.getCard().getBrand());
        } else {
            d.setCardId(null);
            d.setCardLast4(null);
            d.setCardBrand(null);
        }
        d.setUpdatedAt(OffsetDateTime.now().toInstant());
    }

    public static Credit toApi(CreditDocument d){
        Credit c = new Credit()
                .id(d.getId())
                .customerId(d.getCustomerId())
                .type(d.getType()) // enum directo
                .limit(d.getLimit())
                .balance(d.getBalance())
                .interestAnnual(d.getInterestAnnual())
                .dueDate(d.getDueDate() != null ? LocalDate.parse(d.getDueDate()) : null) // String -> LocalDate
                .status(d.getStatus()) // enum directo
                .createdAt(d.getCreatedAt().atOffset(ZoneOffset.UTC))
                .updatedAt(d.getUpdatedAt() == null ? null : d.getUpdatedAt().atOffset(ZoneOffset.UTC));
        if (d.getCardId() != null || d.getCardLast4() != null || d.getCardBrand() != null) {
            c.setCard(new Card().id(d.getCardId()).last4(d.getCardLast4()).brand(d.getCardBrand()));
        }
        return c;
    }
}
