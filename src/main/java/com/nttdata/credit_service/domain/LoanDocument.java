package com.nttdata.credit_service.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Document(collection = "loans")
public class LoanDocument {
    @Id
    private String id;

    @Indexed
    @NotNull
    private String customerId;

    /** PERSONAL | BUSINESS (guardado como texto simple) */
    @Field(targetType = FieldType.STRING) @NotNull
    private String type;

    @Field(name = "limit", targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal limit;

    @Field(targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    @Field(targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal interestAnnual;

    /** Próxima fecha de vencimiento de cuota (si aplica) */
    private LocalDate dueDate;

    /** ACTIVE | CLOSED | DELINQUENT */
    @Field(targetType = FieldType.STRING) @NotNull
    private String status = "ACTIVE";

    private String currency = "PEN";

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant closedAt;
    private String closeReason;
}
