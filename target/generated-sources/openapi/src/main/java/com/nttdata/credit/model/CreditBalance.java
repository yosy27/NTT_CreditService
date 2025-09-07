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
 * Product balances (limit, used, available).
 */
@ApiModel(description = "Product balances (limit, used, available).")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditBalance   {
  @JsonProperty("limit")
  private BigDecimal limit;

  @JsonProperty("balance")
  private BigDecimal balance;

  @JsonProperty("available")
  private BigDecimal available;

  public CreditBalance limit(BigDecimal limit) {
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

  public CreditBalance balance(BigDecimal balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * minimum: 0
   * @return balance
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@DecimalMin("0")
  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public CreditBalance available(BigDecimal available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public BigDecimal getAvailable() {
    return available;
  }

  public void setAvailable(BigDecimal available) {
    this.available = available;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditBalance creditBalance = (CreditBalance) o;
    return Objects.equals(this.limit, creditBalance.limit) &&
        Objects.equals(this.balance, creditBalance.balance) &&
        Objects.equals(this.available, creditBalance.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(limit, balance, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditBalance {\n");
    
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

