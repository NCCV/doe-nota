package br.org.nccv.api.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler for this API.
 */
@ControllerAdvice
public class ApiExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  /**
   * Handle code exception translating into {@link ExceptionResult}.
   *
   * @param ex Code exception
   * @return Response entity with {@link ExceptionResult}
   */
  @ExceptionHandler(CodeException.class)
  public ResponseEntity<ExceptionResult> codeException(CodeException ex) {
    String localizedMessage = messageSource
      .getMessage(ex.getCode(), ex.getValues(), LocaleContextHolder.getLocale());

    ExceptionResult exceptionResult = new ExceptionResult(localizedMessage);
    return ResponseEntity.status(ex.getStatus())
      .body(exceptionResult);
  }
}

