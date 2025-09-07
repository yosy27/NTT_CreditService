package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.nttdata.credit.model.CreditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Credit line stored in the system.
 */
@ApiModel(description = "Credit line stored in the system.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditLineResponse   {
  @JsonProperty("id")
  private String id;

  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("limit")
  private BigDecimal limit;

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("interestAnnual")
  private BigDecimal interestAnnual;

  @JsonProperty("billingCycleDay")
  private Integer billingCycleDay;

  @JsonProperty("paymentDueDay")
  private Integer paymentDueDay;

  @JsonProperty("status")
  private CreditStatus status;

  @JsonProperty("currency")
  private String currency;

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

  public CreditLineResponse id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @ApiModelProperty(required = true, readOnly = true, value = "")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CreditLineResponse customerId(String customerId) {
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

  public CreditLineResponse limit(BigDecimal limit) {
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

  public CreditLineResponse balance(BigDecimal balance) {
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

  public CreditLineResponse interestAnnual(BigDecimal interestAnnual) {
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

  public CreditLineResponse billingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
    return this;
  }

  /**
   * Statement closing day.
   * minimum: 1
   * maximum: 28
   * @return billingCycleDay
  */
  @ApiModelProperty(value = "Statement closing day.")

@Min(1) @Max(28) 
  public Integer getBillingCycleDay() {
    return billingCycleDay;
  }

  public void setBillingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
  }

  public CreditLineResponse paymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
    return this;
  }

  /**
   * Payment due day.
   * minimum: 1
   * maximum: 28
   * @return paymentDueDay
  */
  @ApiModelProperty(value = "Payment due day.")

@Min(1) @Max(28) 
  public Integer getPaymentDueDay() {
    return paymentDueDay;
  }

  public void setPaymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
  }

  public CreditLineResponse status(CreditStatus status) {
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

  public CreditLineResponse currency(String currency) {
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

  public CreditLineResponse closedAt(OffsetDateTime closedAt) {
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

  public CreditLineResponse closeReason(String closeReason) {
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

  public CreditLineResponse createdAt(OffsetDateTime createdAt) {
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

  public CreditLineResponse updatedAt(OffsetDateTime updatedAt) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditLineResponse creditLineResponse = (CreditLineResponse) o;
    return Objects.equals(this.id, creditLineResponse.id) &&
        Objects.equals(this.customerId, creditLineResponse.customerId) &&
        Objects.equals(this.limit, creditLineResponse.limit) &&
        Objects.equals(this.balance, creditLineResponse.balance) &&
        Objects.equals(this.interestAnnual, creditLineResponse.interestAnnual) &&
        Objects.equals(this.billingCycleDay, creditLineResponse.billingCycleDay) &&
        Objects.equals(this.paymentDueDay, creditLineResponse.paymentDueDay) &&
        Objects.equals(this.status, creditLineResponse.status) &&
        Objects.equals(this.currency, creditLineResponse.currency) &&
        Objects.equals(this.closedAt, creditLineResponse.closedAt) &&
        Objects.equals(this.closeReason, creditLineResponse.closeReason) &&
        Objects.equals(this.createdAt, creditLineResponse.createdAt) &&
        Objects.equals(this.updatedAt, creditLineResponse.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, limit, balance, interestAnnual, billingCycleDay, paymentDueDay, status, currency, closedAt, closeReason, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditLineResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    interestAnnual: ").append(toIndentedString(interestAnnual)).append("\n");
    sb.append("    billingCycleDay: ").append(toIndentedString(billingCycleDay)).append("\n");
    sb.append("    paymentDueDay: ").append(toIndentedString(paymentDueDay)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    closedAt: ").append(toIndentedString(closedAt)).append("\n");
    sb.append("    closeReason: ").append(toIndentedString(closeReason)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

