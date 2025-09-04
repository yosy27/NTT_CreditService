package com.nttdata.credit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditUpdate {
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal interestAnnual;

    private LocalDate dueDate;

    /** Solo si aplica para tarjetas. */
    private CardDTO card;
}
