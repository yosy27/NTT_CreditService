//package com.nttdata.credit_service.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.validation.constraints.Pattern;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class CardDTO {
//
//    /** ID/token interno de la tarjeta */
//    private String id;
//
//    /** Últimos 4 dígitos */
//    @Pattern(regexp = "^[0-9]{4}$")
//    private String last4;
//
//    /** VISA | MASTERCARD | AMEX | OTRAS (libre). */
//    private String brand;
//}
