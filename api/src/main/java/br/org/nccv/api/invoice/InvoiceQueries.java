package br.org.nccv.api.invoice;

import br.org.nccv.api.snapshot.SnapshotRepository;

/**
 * Define custom queries for the {@link SnapshotRepository}.
 */
public final class InvoiceQueries {

  public static final String SUM_BY_MONTH = "select date_format(cl_date, '%m/%Y') as date, sum(cl_value) as total " +
    "from invoice group by date_format(cl_date,'%m/%Y') order by date";

  public static final String SUM_BY_YEAR = "select date_format(cl_date, '%Y') as date, sum(cl_value) as total " +
    "from invoice group by date_format(cl_date,'%Y') order by date;";

  private InvoiceQueries() {
  }

}
