package br.org.nccv.api.dashboard;

import br.org.nccv.api.invoice.statistics.SimpleStatistic;

import java.util.List;
import java.util.Objects;

/**
 * Default dashboard values.
 */
public class DefaultDashboard {

  private List<SimpleStatistic> invoiceTotalByMonth;
  private List<SimpleStatistic> invoiceTotalByYear;

  private DefaultDashboard(Builder builder) {
    this.invoiceTotalByMonth = builder.invoiceTotalByMonth;
    this.invoiceTotalByYear = builder.invoiceTotalByYear;
  }

  public List<SimpleStatistic> getInvoiceTotalByMonth() {
    return invoiceTotalByMonth;
  }

  public List<SimpleStatistic> getInvoiceTotalByYear() {
    return invoiceTotalByYear;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private List<SimpleStatistic> invoiceTotalByMonth;
    private List<SimpleStatistic> invoiceTotalByYear;

    public Builder withInvoiceTotalByMonth(List<SimpleStatistic> invoiceTotalByMonth) {
      this.invoiceTotalByMonth = invoiceTotalByMonth;
      return this;
    }

    public Builder withInvoiceTotalByYear(List<SimpleStatistic> invoiceTotalByYear) {
      this.invoiceTotalByYear = invoiceTotalByYear;
      return this;
    }

    public DefaultDashboard build() {
      Objects.requireNonNull(invoiceTotalByMonth);
      Objects.requireNonNull(invoiceTotalByYear);

      return new DefaultDashboard(this);
    }

  }
}
