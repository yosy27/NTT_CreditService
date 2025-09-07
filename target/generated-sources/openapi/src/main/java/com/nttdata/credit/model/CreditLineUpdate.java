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
 * Allowed fields for partial update.
 */
@ApiModel(description = "Allowed fields for partial update.")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditLineUpdate   {
  @JsonProperty("interestAnnual")
  private BigDecimal interestAnnual;

  @JsonProperty("billingCycleDay")
  private Integer billingCycleDay;

  @JsonProperty("paymentDueDay")
  private Integer paymentDueDay;

  public CreditLineUpdate interestAnnual(BigDecimal interestAnnual) {
    this.interestAnnual = interestAnnual;
    return this;
  }

  /**
   * Get interestAnnual
   * minimum: 0
   * @return interestAnnual
  */
  @ApiModelProperty(value = "")

  @Valid
@DecimalMin("0")
  public BigDecimal getInterestAnnual() {
    return interestAnnual;
  }

  public void setInterestAnnual(BigDecimal interestAnnual) {
    this.interestAnnual = interestAnnual;
  }

  public CreditLineUpdate billingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
    return this;
  }

  /**
   * Get billingCycleDay
   * minimum: 1
   * maximum: 28
   * @return billingCycleDay
  */
  @ApiModelProperty(value = "")

@Min(1) @Max(28) 
  public Integer getBillingCycleDay() {
    return billingCycleDay;
  }

  public void setBillingCycleDay(Integer billingCycleDay) {
    this.billingCycleDay = billingCycleDay;
  }

  public CreditLineUpdate paymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
    return this;
  }

  /**
   * Get paymentDueDay
   * minimum: 1
   * maximum: 28
   * @return paymentDueDay
  */
  @ApiModelProperty(value = "")

@Min(1) @Max(28) 
  public Integer getPaymentDueDay() {
    return paymentDueDay;
  }

  public void setPaymentDueDay(Integer paymentDueDay) {
    this.paymentDueDay = paymentDueDay;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditLineUpdate creditLineUpdate = (CreditLineUpdate) o;
    return Objects.equals(this.interestAnnual, creditLineUpdate.interestAnnual) &&
        Objects.equals(this.billingCycleDay, creditLineUpdate.billingCycleDay) &&
        Objects.equals(this.paymentDueDay, creditLineUpdate.paymentDueDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interestAnnual, billingCycleDay, paymentDueDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditLineUpdate {\n");
    
    sb.append("    interestAnnual: ").append(toIndentedString(interestAnnual)).append("\n");
    sb.append("    billingCycleDay: ").append(toIndentedString(billingCycleDay)).append("\n");
    sb.append("    paymentDueDay: ").append(toIndentedString(paymentDueDay)).append("\n");
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

