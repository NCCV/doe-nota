package br.org.nccv.api.invoice;

import br.org.nccv.api.invoice.statistics.SimpleStatistic;
import br.org.nccv.api.support.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Snapshot statistics repository.
 */
public interface InvoiceRepository extends BaseRepository<Invoice, String> {

  /**
   * Sum by month the values from invoice.
   */
  @Query(value = InvoiceQueries.SUM_BY_MONTH, nativeQuery = true)
  List<SimpleStatistic> sumByMonth();

  /**
   * Sum by year the values from invoice.
   */
  @Query(value = InvoiceQueries.SUM_BY_YEAR, nativeQuery = true)
  List<SimpleStatistic> sumByYear();

}
