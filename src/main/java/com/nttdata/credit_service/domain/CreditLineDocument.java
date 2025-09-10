package com.nttdata.credit_service.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document(collection = "credit_lines")
public class CreditLineDocument {

    @Id
    private String id;

    @Indexed
    @NotNull
    private String customerId;

    @Field(name = "limit", targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal limit;

    @Field(targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    @Field(targetType = FieldType.DECIMAL128) @NotNull
    private BigDecimal interestAnnual;

    /** Día de cierre de estado de cuenta [1..28] */
    @Min(1) @Max(28)
    private Integer billingCycleDay;

    /** Día de vencimiento de pago [1..28] */
    @Min(1) @Max(28)
    private Integer paymentDueDay;

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
