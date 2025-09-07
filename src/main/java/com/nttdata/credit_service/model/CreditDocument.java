//package com.nttdata.credit_service.model;
//
//import com.nttdata.credit.model.CreditStatus;
//import com.nttdata.credit.model.CreditType;
//
//import lombok.Data;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.annotation.Version;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//import org.springframework.data.mongodb.core.mapping.Field;
//import org.springframework.data.mongodb.core.mapping.FieldType;
//
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDate;
//
//
//@Data
//@Document(collection = "credits")
//public class CreditDocument {
//    @Id
//    private String id;
//
//    @Indexed
//    @NotNull
//    private String customerId;
//
//    @Field(targetType = FieldType.STRING) @NotNull
//    private String type; // PERSONAL/BUSINESS/CREDIT_CARD
//
//    @Field(name = "limit", targetType = FieldType.DECIMAL128) @NotNull
//    private BigDecimal limit;
//
//    @Field(targetType = FieldType.DECIMAL128) @NotNull
//    private BigDecimal balance;
//
//    @Field(targetType = FieldType.DECIMAL128) @NotNull
//    private BigDecimal interestAnnual;
//
//    private LocalDate dueDate;
//
//    @Field(targetType = FieldType.STRING) @NotNull
//    private String status; // ACTIVE/CLOSED/DELINQUENT
//
//    private String cardId, cardBrand;
//
//    @Pattern(regexp="^[0-9]{4}$")
//    private String cardLast4;
//    private String currency = "PEN";
//
//    @CreatedDate
//    private Instant createdAt;
//    @LastModifiedDate
//    private Instant updatedAt;
//    private Instant closedAt;
//    private String closeReason;
//
//}
