package br.org.nccv.api.snapshot.job.export.csv;

import br.org.nccv.api.invoice.Invoice;
import br.org.nccv.api.invoice.InvoiceCsv;
import br.org.nccv.api.invoice.InvoiceDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Process {@link InvoiceDocument}.
 */
@Component
public class ExportCsvItemProcessor implements ItemProcessor<Invoice, InvoiceCsv> {

  /**
   * Convert {@link InvoiceDocument} into {@link Invoice}.
   *
   * @param invoice The {@link InvoiceDocument}
   * @return A converted {@link Invoice}
   */
  @Override
  public InvoiceCsv process(Invoice invoice) {
    return new InvoiceCsv(invoice);
  }
}