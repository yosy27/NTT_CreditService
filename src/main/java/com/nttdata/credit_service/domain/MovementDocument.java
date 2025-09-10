package com.nttdata.credit_service.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document(collection = "movements")
public class MovementDocument {

    @Id
    private String id;

    @Indexed
    private String creditId;

    /** CHARGE, PAYMENT, INTEREST, FEE, REVERSAL */
    @Field(targetType = FieldType.STRING)
    private String type;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal amount;

    private Instant txnAt;      // fecha de la transacción
    private Instant postedAt;   // fecha de contabilización

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal runningBalance;

    private String channel;
    private String productKind; // "LOAN" | "CREDIT_LINE"

}
