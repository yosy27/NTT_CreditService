package com.nttdata.credit_service.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("transactions")
public class TransactionDocument {
    @Id
    private String id;

    @Indexed(unique = true)
    private String movementId;     // clave idempotente: el movimiento de crédito

    private String creditId;       // producto origen (crédito)
    private String productType;
    private String productNumber;  // opcional si lo tienes

    private String type;  // PAYMENT | CHARGE
    private BigDecimal amount;
    private String currency;

    @CreatedDate
    private Instant createdDate;   // fecha de la transacción

    // Opcionales del YAML: si no los usas, déjalos null
    private Person holder;
    private Person signatory;

}
