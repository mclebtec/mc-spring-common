package com.mclebtec.error.domain.exception;

import com.mclebtec.error.domain.enums.ValidationErrors;
import lombok.Getter;

@Getter
public class WebException extends RuntimeException {

  private final String errorText;
  private final String errorCode;


  public WebException(final ValidationErrors error) {
    super(error.getStatusText());
    this.errorText = error.getStatusText();
    this.errorCode = error.getErrorCode();
  }

  public WebException(final ValidationErrors error, final String message) {
    super(message);
    this.errorText = error.getStatusText();
    this.errorCode = error.getErrorCode();
  }

  public WebException(final ValidationErrors error, final String message, final Throwable ex) {
    super(message, ex);
    this.errorText = error.getStatusText();
    this.errorCode = error.getErrorCode();
  }

}
