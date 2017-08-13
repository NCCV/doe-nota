package br.org.nccv.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CodeException {

  protected NotFoundException(Builder<?> builder) {
    super(builder);
  }

  public abstract static class Builder<T extends Builder<T>> extends CodeException.Builder<T> {

    @Override
    public NotFoundException build() {
      return new NotFoundException(this);
    }
  }

  private static class DefaultBuilder extends Builder<DefaultBuilder> {

    @Override
    protected DefaultBuilder self() {
      return this;
    }
  }

  public static NotFoundException fromCode(String code) {
    return from(code)
      .build();
  }

  public static Builder<?> from(String code) {
    return builder()
      .withStatus(HttpStatus.NOT_FOUND)
      .withCode(code);
  }

  public static Builder<?> builder() {
    return new DefaultBuilder();
  }
}
