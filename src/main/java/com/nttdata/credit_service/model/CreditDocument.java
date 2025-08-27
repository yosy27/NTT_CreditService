package com.nttdata.credit_service.model;

import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.CreditType;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad de persistencia que representa un producto de crédito
 * en la colección "credits" de MongoDB.
**/
@Data
@Document(collection = "credits")
public class CreditDocument {
    @Id
    private String id;
    private String customerId;
    private CreditType type;
    private Double limit;
    private Double balance;
    private Double interestAnnual;
    private String dueDate;
    private CreditStatus status;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
    private String cardId, cardLast4, cardBrand;
}
