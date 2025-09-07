package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Payment data (owner or third-party).
 */
@ApiModel(description = "Payment data (owner or third-party).")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditPaymentRequest   {
  @JsonProperty("payerCustomerId")
  private String payerCustomerId;

  @JsonProperty("amount")
  private BigDecimal amount;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    CASH("CASH"),
    
    TRANSFER("TRANSFER"),
    
    CARD("CARD");

    private String value;

    ChannelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ChannelEnum fromValue(String value) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("channel")
  private ChannelEnum channel;

  @JsonProperty("note")
  private String note;

  public CreditPaymentRequest payerCustomerId(String payerCustomerId) {
    this.payerCustomerId = payerCustomerId;
    return this;
  }

  /**
   * Payer customer id (optional).
   * @return payerCustomerId
  */
  @ApiModelProperty(value = "Payer customer id (optional).")


  public String getPayerCustomerId() {
    return payerCustomerId;
  }

  public void setPayerCustomerId(String payerCustomerId) {
    this.payerCustomerId = payerCustomerId;
  }

  public CreditPaymentRequest amount(BigDecimal amount) {
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

  public CreditPaymentRequest channel(ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
  */
  @ApiModelProperty(value = "")


  public ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(ChannelEnum channel) {
    this.channel = channel;
  }

  public CreditPaymentRequest note(String note) {
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
    CreditPaymentRequest creditPaymentRequest = (CreditPaymentRequest) o;
    return Objects.equals(this.payerCustomerId, creditPaymentRequest.payerCustomerId) &&
        Objects.equals(this.amount, creditPaymentRequest.amount) &&
        Objects.equals(this.channel, creditPaymentRequest.channel) &&
        Objects.equals(this.note, creditPaymentRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payerCustomerId, amount, channel, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditPaymentRequest {\n");
    
    sb.append("    payerCustomerId: ").append(toIndentedString(payerCustomerId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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

