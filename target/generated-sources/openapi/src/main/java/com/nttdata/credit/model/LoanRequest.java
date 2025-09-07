package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.nttdata.credit.model.DocumentType;
import com.nttdata.credit.model.LoanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Loan creation payload.
 */
@ApiModel(description = "Loan creation payload.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class LoanRequest   {
  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("documentType")
  private DocumentType documentType;

  @JsonProperty("documentNumber")
  private String documentNumber;

  @JsonProperty("type")
  private LoanType type;

  @JsonProperty("limit")
  private BigDecimal limit;

  @JsonProperty("interestAnnual")
  private BigDecimal interestAnnual;

  @JsonProperty("dueDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate dueDate;

  @JsonProperty("currency")
  private String currency = "PEN";

  public LoanRequest customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Internal customer id.
   * @return customerId
  */
  @ApiModelProperty(value = "Internal customer id.")


  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public LoanRequest documentType(DocumentType documentType) {
    this.documentType = documentType;
    return this;
  }

  /**
   * Get documentType
   * @return documentType
  */
  @ApiModelProperty(value = "")

  @Valid

  public DocumentType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(DocumentType documentType) {
    this.documentType = documentType;
  }

  public LoanRequest documentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
    return this;
  }

  /**
   * Get documentNumber
   * @return documentNumber
  */
  @ApiModelProperty(value = "")


  public String getDocumentNumber() {
    return documentNumber;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public LoanRequest type(LoanType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public LoanType getType() {
    return type;
  }

  public void setType(LoanType type) {
    this.type = type;
  }

  public LoanRequest limit(BigDecimal limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Get limit
   * minimum: 0
   * @return limit
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@DecimalMin("0")
  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }

  public LoanRequest interestAnnual(BigDecimal interestAnnual) {
    this.interestAnnual = interestAnnual;
    return this;
  }

  /**
   * Get interestAnnual
   * minimum: 0
   * @return interestAnnual
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@DecimalMin("0")
  public BigDecimal getInterestAnnual() {
    return interestAnnual;
  }

  public void setInterestAnnual(BigDecimal interestAnnual) {
    this.interestAnnual = interestAnnual;
  }

  public LoanRequest dueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
    return this;
  }

  /**
   * Next payment due date.
   * @return dueDate
  */
  @ApiModelProperty(value = "Next payment due date.")

  @Valid

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LoanRequest currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
  */
  @ApiModelProperty(example = "PEN", value = "")


  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoanRequest loanRequest = (LoanRequest) o;
    return Objects.equals(this.customerId, loanRequest.customerId) &&
        Objects.equals(this.documentType, loanRequest.documentType) &&
        Objects.equals(this.documentNumber, loanRequest.documentNumber) &&
        Objects.equals(this.type, loanRequest.type) &&
        Objects.equals(this.limit, loanRequest.limit) &&
        Objects.equals(this.interestAnnual, loanRequest.interestAnnual) &&
        Objects.equals(this.dueDate, loanRequest.dueDate) &&
        Objects.equals(this.currency, loanRequest.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, documentType, documentNumber, type, limit, interestAnnual, dueDate, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanRequest {\n");
    
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    documentType: ").append(toIndentedString(documentType)).append("\n");
    sb.append("    documentNumber: ").append(toIndentedString(documentNumber)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    interestAnnual: ").append(toIndentedString(interestAnnual)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

