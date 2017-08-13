package br.org.nccv.api.exception;

import org.springframework.http.HttpStatus;

public class CodeException extends RuntimeException {

  private final HttpStatus status;
  private final String code;
  private final Object[] values;

  protected CodeException(Builder<?> builder) {
    this.status = builder.status;
    this.code = builder.code;
    this.values = builder.values;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public Object[] getValues() {
    return values;
  }

  public abstract static class Builder<T extends Builder<T>> {

    private HttpStatus status;
    private String code;
    private Object[] values;

    public T withStatus(HttpStatus status) {
      this.status = status;
      return self();
    }

    public T withCode(String code) {
      this.code = code;
      return self();
    }

    public T withValues(Object... values) {
      this.values = values;
      return self();
    }

    protected abstract T self();

    public CodeException build() {
      return new CodeException(this);
    }
  }

  private static class DefaultBuilder extends Builder<DefaultBuilder> {

    @Override
    protected DefaultBuilder self() {
      return this;
    }
  }

  public static Builder<?> builder() {
    return new DefaultBuilder();
  }
}
