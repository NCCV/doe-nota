package br.org.nccv.api.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionResult {

  private String timestamp;
  private String message;

  public ExceptionResult(String message) {
    this.timestamp = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
    this.message = message;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

}
