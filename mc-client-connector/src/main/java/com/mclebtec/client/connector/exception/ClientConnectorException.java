package com.mclebtec.client.connector.exception;

import com.mclebtec.error.domain.enums.ValidationErrors;
import com.mclebtec.error.domain.exception.WebException;

public class ClientConnectorException extends WebException {

  public ClientConnectorException(ValidationErrors error) {
    super(error);
  }

  public ClientConnectorException(ValidationErrors error, String message) {
    super(error, message);
  }

  public ClientConnectorException(ValidationErrors error, String message,
      Throwable ex) {
    super(error, message, ex);
  }

}
