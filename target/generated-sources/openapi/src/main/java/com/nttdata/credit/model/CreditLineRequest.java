package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Credit line creation payload.
 */
@ApiModel(description = "Credit line creation payload.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditLineRequest   {
  @JsonProperty("customerId")
  private String customerId;

  @JsonProperty("limit")
  private BigDecimal limit;

  @JsonProperty("interestAnnual")
  private BigDecimal interestAnnual;

  @JsonProperty("billingCycleDay")
  private Integer billingCycleDay;

  @JsonProperty("paymentDueDay")
  private Integer paymentDueDay;

  @JsonProperty("currency")
  private String currency = "PEN";

  public CreditLineRequest customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public CreditLineRequest limit(BigDecimal limit) {
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

  public CreditLineRequest interestAnnual(BigDecimal interestAnnual) {
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

  public CreditLineRequest billingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
    return this;
  }

  /**
   * Get billingCycleDay
   * minimum: 1
   * maximum: 28
   * @return billingCycleDay
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Min(1) @Max(28) 
  public Integer getBillingCycleDay() {
    return billingCycleDay;
  }

  public void setBillingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
  }

  public CreditLineRequest paymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
    return this;
  }

  /**
   * Get paymentDueDay
   * minimum: 1
   * maximum: 28
   * @return paymentDueDay
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Min(1) @Max(28) 
  public Integer getPaymentDueDay() {
    return paymentDueDay;
  }

  public void setPaymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
  }

  public CreditLineRequest currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
  */
  @ApiModelProperty(example = "PEN", required = true, value = "")
  @NotNull


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
    CreditLineRequest creditLineRequest = (CreditLineRequest) o;
    return Objects.equals(this.customerId, creditLineRequest.customerId) &&
        Objects.equals(this.limit, creditLineRequest.limit) &&
        Objects.equals(this.interestAnnual, creditLineRequest.interestAnnual) &&
        Objects.equals(this.billingCycleDay, creditLineRequest.billingCycleDay) &&
        Objects.equals(this.paymentDueDay, creditLineRequest.paymentDueDay) &&
        Objects.equals(this.currency, creditLineRequest.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, limit, interestAnnual, billingCycleDay, paymentDueDay, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditLineRequest {\n");
    
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    interestAnnual: ").append(toIndentedString(interestAnnual)).append("\n");
    sb.append("    billingCycleDay: ").append(toIndentedString(billingCycleDay)).append("\n");
    sb.append("    paymentDueDay: ").append(toIndentedString(paymentDueDay)).append("\n");
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

