package br.org.nccv.api.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends CodeException {

  protected BadRequestException(Builder<?> builder) {
    super(builder);
  }

  public abstract static class Builder<T extends Builder<T>> extends CodeException.Builder<T> {

    @Override
    public BadRequestException build() {
      return new BadRequestException(this);
    }
  }

  private static class DefaultBuilder extends Builder<DefaultBuilder> {

    @Override
    protected DefaultBuilder self() {
      return this;
    }
  }

  public static BadRequestException fromCode(String code) {
    return from(code)
      .build();
  }

  public static Builder<?> from(String code) {
    return builder()
      .withStatus(HttpStatus.BAD_REQUEST)
      .withCode(code);
  }

  public static Builder<?> builder() {
    return new DefaultBuilder();
  }
}
