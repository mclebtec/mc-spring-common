package com.mclebtec.client.connector.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Common {

  BEARER("Bearer "),
  TOKEN("token"),
  AZURE_API_KEY("api-key");

  private final String key;

}

