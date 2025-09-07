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
 * Charge data (card purchase).
 */
@ApiModel(description = "Charge data (card purchase).")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditChargeRequest   {
  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("merchant")
  private String merchant;

  @JsonProperty("channel")
  private String channel = "CARD";

  @JsonProperty("note")
  private String note;

  public CreditChargeRequest amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * minimum: 0.01
   * @return amount
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
@DecimalMin("0.01")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public CreditChargeRequest merchant(String merchant) {
    this.merchant = merchant;
    return this;
  }

  /**
   * Get merchant
   * @return merchant
  */
  @ApiModelProperty(value = "")

@Size(max=120) 
  public String getMerchant() {
    return merchant;
  }

  public void setMerchant(String merchant) {
    this.merchant = merchant;
  }

  public CreditChargeRequest channel(String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
  */
  @ApiModelProperty(value = "")


  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public CreditChargeRequest note(String note) {
    this.note = note;
    return this;
  }

  /**
   * Get note
   * @return note
  */
  @ApiModelProperty(value = "")

@Size(max=200) 
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditChargeRequest creditChargeRequest = (CreditChargeRequest) o;
    return Objects.equals(this.amount, creditChargeRequest.amount) &&
        Objects.equals(this.merchant, creditChargeRequest.merchant) &&
        Objects.equals(this.channel, creditChargeRequest.channel) &&
        Objects.equals(this.note, creditChargeRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, merchant, channel, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditChargeRequest {\n");
    
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    merchant: ").append(toIndentedString(merchant)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

