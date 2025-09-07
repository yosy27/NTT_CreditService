package com.nttdata.credit.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Business code.
 */
public enum ErrorCode {
  
  CREDIT_PERSONAL_LIMIT("CREDIT_PERSONAL_LIMIT"),
  
  CREDIT_DELINQUENT_LOCK("CREDIT_DELINQUENT_LOCK"),
  
  CREDIT_CARD_LIMIT_EXCEEDED("CREDIT_CARD_LIMIT_EXCEEDED"),
  
  CREDIT_INVALID_STATE("CREDIT_INVALID_STATE"),
  
  CREDIT_NOT_FOUND("CREDIT_NOT_FOUND"),
  
  CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND"),
  
  VALIDATION_ERROR("VALIDATION_ERROR"),
  
  SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE");

  private String value;

  ErrorCode(String value) {
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
  public static ErrorCode fromValue(String value) {
    for (ErrorCode b : ErrorCode.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

