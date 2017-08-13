package br.org.nccv.api.dashboard;

import br.org.nccv.api.invoice.statistics.SimpleStatistic;
import br.org.nccv.api.invoice.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Summarize data for dashboard.
 */
@Service
public class DashboardService {

  @Autowired
  private InvoiceRepository invoiceRepository;

  /**
   * Summarize the data to the default dashboard.
   *
   * @return Default dashboard
   */
  @Transactional(readOnly = true)
  public DefaultDashboard summarize() {
    List<SimpleStatistic> byMonth = invoiceRepository.sumByMonth();
    List<SimpleStatistic> byYear = invoiceRepository.sumByYear();

    return DefaultDashboard.builder()
      .withInvoiceTotalByMonth(byMonth)
      .withInvoiceTotalByYear(byYear)
      .build();
  }

}
