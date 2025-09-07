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
 * OverdueStatus
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class OverdueStatus   {
  @JsonProperty("hasOverdue")
  private Boolean hasOverdue;

  @JsonProperty("totalOverdueAmount")
  private BigDecimal totalOverdueAmount;

  public OverdueStatus hasOverdue(Boolean hasOverdue) {
    this.hasOverdue = hasOverdue;
    return this;
  }

  /**
   * True if at least one product is DELINQUENT.
   * @return hasOverdue
  */
  @ApiModelProperty(required = true, value = "True if at least one product is DELINQUENT.")
  @NotNull


  public Boolean getHasOverdue() {
    return hasOverdue;
  }

  public void setHasOverdue(Boolean hasOverdue) {
    this.hasOverdue = hasOverdue;
  }

  public OverdueStatus totalOverdueAmount(BigDecimal totalOverdueAmount) {
    this.totalOverdueAmount = totalOverdueAmount;
    return this;
  }

  /**
   * Sum of balances for all DELINQUENT products.
   * @return totalOverdueAmount
  */
  @ApiModelProperty(required = true, value = "Sum of balances for all DELINQUENT products.")
  @NotNull

  @Valid

  public BigDecimal getTotalOverdueAmount() {
    return totalOverdueAmount;
  }

  public void setTotalOverdueAmount(BigDecimal totalOverdueAmount) {
    this.totalOverdueAmount = totalOverdueAmount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OverdueStatus overdueStatus = (OverdueStatus) o;
    return Objects.equals(this.hasOverdue, overdueStatus.hasOverdue) &&
        Objects.equals(this.totalOverdueAmount, overdueStatus.totalOverdueAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hasOverdue, totalOverdueAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OverdueStatus {\n");
    
    sb.append("    hasOverdue: ").append(toIndentedString(hasOverdue)).append("\n");
    sb.append("    totalOverdueAmount: ").append(toIndentedString(totalOverdueAmount)).append("\n");
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

