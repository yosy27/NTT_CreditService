package com.nttdata.credit.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Movement (charge, payment, interest, fee or reversal).
 */
@ApiModel(description = "Movement (charge, payment, interest, fee or reversal).")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-09-06T19:14:38.721215100-05:00[America/Lima]")
public class CreditMovement   {
  @JsonProperty("id")
  private String id;

  @JsonProperty("creditId")
  private String creditId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CHARGE("CHARGE"),
    
    PAYMENT("PAYMENT"),
    
    INTEREST("INTEREST"),
    
    FEE("FEE"),
    
    REVERSAL("REVERSAL");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("type")
  private TypeEnum type;

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("txnAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime txnAt;

  @JsonProperty("postedAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime postedAt;

  @JsonProperty("runningBalance")
  private BigDecimal runningBalance;

  @JsonProperty("channel")
  private String channel;

  public CreditMovement id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Movement id
   * @return id
  */
  @ApiModelProperty(required = true, value = "Movement id")
  @NotNull


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CreditMovement creditId(String creditId) {
    this.creditId = creditId;
    return this;
  }

  /**
   * Product id (loan or credit line)
   * @return creditId
  */
  @ApiModelProperty(required = true, value = "Product id (loan or credit line)")
  @NotNull


  public String getCreditId() {
    return creditId;
  }

  public void setCreditId(String creditId) {
    this.creditId = creditId;
  }

  public CreditMovement type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull


  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public CreditMovement amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public CreditMovement txnAt(OffsetDateTime txnAt) {
    this.txnAt = txnAt;
    return this;
  }

  /**
   * Get txnAt
   * @return txnAt
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public OffsetDateTime getTxnAt() {
    return txnAt;
  }

  public void setTxnAt(OffsetDateTime txnAt) {
    this.txnAt = txnAt;
  }

  public CreditMovement postedAt(OffsetDateTime postedAt) {
    this.postedAt = postedAt;
    return this;
  }

  /**
   * Get postedAt
   * @return postedAt
  */
  @ApiModelProperty(value = "")

  @Valid

  public OffsetDateTime getPostedAt() {
    return postedAt;
  }

  public void setPostedAt(OffsetDateTime postedAt) {
    this.postedAt = postedAt;
  }

  public CreditMovement runningBalance(BigDecimal runningBalance) {
    this.runningBalance = runningBalance;
    return this;
  }

  /**
   * Get runningBalance
   * @return runningBalance
  */
  @ApiModelProperty(value = "")

  @Valid

  public BigDecimal getRunningBalance() {
    return runningBalance;
  }

  public void setRunningBalance(BigDecimal runningBalance) {
    this.runningBalance = runningBalance;
  }

  public CreditMovement channel(String channel) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreditMovement creditMovement = (CreditMovement) o;
    return Objects.equals(this.id, creditMovement.id) &&
        Objects.equals(this.creditId, creditMovement.creditId) &&
        Objects.equals(this.type, creditMovement.type) &&
        Objects.equals(this.amount, creditMovement.amount) &&
        Objects.equals(this.txnAt, creditMovement.txnAt) &&
        Objects.equals(this.postedAt, creditMovement.postedAt) &&
        Objects.equals(this.runningBalance, creditMovement.runningBalance) &&
        Objects.equals(this.channel, creditMovement.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creditId, type, amount, txnAt, postedAt, runningBalance, channel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreditMovement {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    creditId: ").append(toIndentedString(creditId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    txnAt: ").append(toIndentedString(txnAt)).append("\n");
    sb.append("    postedAt: ").append(toIndentedString(postedAt)).append("\n");
    sb.append("    runningBalance: ").append(toIndentedString(runningBalance)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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

