package com.nttdata.credit_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCreate {

    @NotBlank
    private String customerId;

    @NotBlank
    @Pattern(regexp = "PERSONAL|BUSINESS|CREDIT_CARD",
            message = "type debe ser PERSONAL, BUSINESS o CREDIT_CARD")
    private String type;

    @NotNull
    @DecimalMin(value="0.00", inclusive = true)
    private BigDecimal limit;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal interestAnnual;

    private LocalDate dueDate;

    @NotBlank @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;       // PEN|USD

    private CardDTO card;
}
