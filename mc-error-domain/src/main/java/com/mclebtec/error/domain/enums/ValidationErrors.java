package com.mclebtec.error.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationErrors {

  CONNECTIVITY_ERROR("001", "Client Connection Issue");

  private final String errorCode;
  private final String statusText;

}
