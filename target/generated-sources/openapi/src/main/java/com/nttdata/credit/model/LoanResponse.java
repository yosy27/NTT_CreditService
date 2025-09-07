package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.nttdata.credit.model.CreditStatus;
import com.nttdata.credit.model.LoanType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Loan stored in the system.
 */
@ApiModel(description = "Loan stored in the system.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class LoanResponse   {
  @JsonProperty("id")
  private String id;

  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("type")
  private LoanType type;

  @JsonProperty("limit")
  private BigDecimal limit;

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("interestAnnual")
  private BigDecimal interestAnnual;

  @JsonProperty("dueDate")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
  private LocalDate dueDate;

  @JsonProperty("status")
  private CreditStatus status;

  @JsonProperty("closedAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime closedAt;

  @JsonProperty("closeReason")
  private String closeReason;

  @JsonProperty("createdAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @JsonProperty("updatedAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  @JsonProperty("currency")
  private String currency;

  public LoanResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Loan id
   * @return id
  */
  @ApiModelProperty(required = true, readOnly = true, value = "Loan id")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LoanResponse customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  @ApiModelProperty(required = true, readOnly = true, value = "")
  @NotNull


  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public LoanResponse type(LoanType type) {
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

  public LoanResponse limit(BigDecimal limit) {
    this.limit = limit;
    return this;
  }

  /**
   * Approved amount (max).
   * minimum: 0
   * @return limit
  */
  @ApiModelProperty(required = true, value = "Approved amount (max).")
  @NotNull

  @Valid
@DecimalMin("0")
  public BigDecimal getLimit() {
    return limit;
  }

  public void setLimit(BigDecimal limit) {
    this.limit = limit;
  }

  public LoanResponse balance(BigDecimal balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * minimum: 0
   * @return balance
  */
  @ApiModelProperty(required = true, readOnly = true, value = "")
  @NotNull

  @Valid
@DecimalMin("0")
  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public LoanResponse interestAnnual(BigDecimal interestAnnual) {
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

  public LoanResponse dueDate(LocalDate dueDate) {
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

  public LoanResponse status(CreditStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public CreditStatus getStatus() {
    return status;
  }

  public void setStatus(CreditStatus status) {
    this.status = status;
  }

  public LoanResponse closedAt(OffsetDateTime closedAt) {
    this.closedAt = closedAt;
    return this;
  }

  /**
   * Get closedAt
   * @return closedAt
  */
  @ApiModelProperty(readOnly = true, value = "")

  @Valid

  public OffsetDateTime getClosedAt() {
    return closedAt;
  }

  public void setClosedAt(OffsetDateTime closedAt) {
    this.closedAt = closedAt;
  }

  public LoanResponse closeReason(String closeReason) {
    this.closeReason = closeReason;
    return this;
  }

  /**
   * Get closeReason
   * @return closeReason
  */
  @ApiModelProperty(readOnly = true, value = "")


  public String getCloseReason() {
    return closeReason;
  }

  public void setCloseReason(String closeReason) {
    this.closeReason = closeReason;
  }

  public LoanResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @ApiModelProperty(required = true, readOnly = true, value = "")
  @NotNull

  @Valid

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LoanResponse updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @ApiModelProperty(readOnly = true, value = "")

  @Valid

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LoanResponse currency(String currency) {
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
    LoanResponse loanResponse = (LoanResponse) o;
    return Objects.equals(this.id, loanResponse.id) &&
        Objects.equals(this.customerId, loanResponse.customerId) &&
        Objects.equals(this.type, loanResponse.type) &&
        Objects.equals(this.limit, loanResponse.limit) &&
        Objects.equals(this.balance, loanResponse.balance) &&
        Objects.equals(this.interestAnnual, loanResponse.interestAnnual) &&
        Objects.equals(this.dueDate, loanResponse.dueDate) &&
        Objects.equals(this.status, loanResponse.status) &&
        Objects.equals(this.closedAt, loanResponse.closedAt) &&
        Objects.equals(this.closeReason, loanResponse.closeReason) &&
        Objects.equals(this.createdAt, loanResponse.createdAt) &&
        Objects.equals(this.updatedAt, loanResponse.updatedAt) &&
        Objects.equals(this.currency, loanResponse.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, type, limit, balance, interestAnnual, dueDate, status, closedAt, closeReason, createdAt, updatedAt, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoanResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    interestAnnual: ").append(toIndentedString(interestAnnual)).append("\n");
    sb.append("    dueDate: ").append(toIndentedString(dueDate)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    closedAt: ").append(toIndentedString(closedAt)).append("\n");
    sb.append("    closeReason: ").append(toIndentedString(closeReason)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

