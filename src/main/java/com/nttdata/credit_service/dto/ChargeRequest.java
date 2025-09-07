//package com.nttdata.credit_service.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.DecimalMin;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.math.BigDecimal;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//
//public class ChargeRequest {
//    @NotNull
//    @DecimalMin(value = "0.01", inclusive = true)
//    private BigDecimal amount;
//
//    @Size(max = 120)
//    private String merchant;
//
//    /** Canal del consumo */
//    @NotBlank
//    private String channel;
//
//    @Size(max = 200)
//    private String note;
//}
