//package com.nttdata.credit_service.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.*;
//import java.math.BigDecimal;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//
//public class PaymentRequest {
//
//    /** Si se omite, se asume el dueño del crédito. */
//    private String payerCustomerId;
//
//    @NotNull
//    @DecimalMin(value = "0.01", inclusive = true)
//    private BigDecimal amount;
//
//    /** CASH | TRANSFER | CARD */
//    @NotBlank
//    @Pattern(regexp = "CASH|TRANSFER|CARD")
//    private String channel;
//
//    @Size(max = 200)
//    private String note;
//}
