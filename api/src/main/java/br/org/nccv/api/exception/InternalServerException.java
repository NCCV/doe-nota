package br.org.nccv.api.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends CodeException {

  protected InternalServerException(Builder<?> builder) {
    super(builder);
  }

  public abstract static class Builder<T extends Builder<T>> extends CodeException.Builder<T> {

    @Override
    public InternalServerException build() {
      return new InternalServerException(this);
    }
  }

  private static class DefaultBuilder extends Builder<DefaultBuilder> {

    @Override
    protected DefaultBuilder self() {
      return this;
    }
  }

  public static InternalServerException fromCode(String code) {
    return from(code)
      .build();
  }

  public static Builder<?> from(String code) {
    return builder()
      .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
      .withCode(code);
  }

  public static Builder<?> builder() {
    return new DefaultBuilder();
  }
}
