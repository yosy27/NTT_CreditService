package com.nttdata.credit_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document("credit_movements")
public class CreditMovementDocument {
    @Id
    private String id;
    @Indexed
    private String creditId;
    @Field(targetType = FieldType.STRING)
    private String type;  // CHARGE, PAYMENT, INTEREST, FEE, REVERSAL
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;
    private Instant txnAt;
    private Instant postedAt;
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal runningBalance;
    private String channel;
}
