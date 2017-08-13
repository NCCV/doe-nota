package br.org.nccv.api.snapshot.job.snapshot;

import br.org.nccv.api.invoice.Invoice;
import br.org.nccv.api.invoice.InvoiceDocument;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Process {@link InvoiceDocument}.
 */
@Component
public class SnapshotItemProcessor implements ItemProcessor<InvoiceDocument, Invoice> {

  /**
   * Convert {@link InvoiceDocument} into {@link Invoice}.
   *
   * @param document The {@link InvoiceDocument}
   * @return A converted {@link Invoice}
   */
  @Override
  public Invoice process(InvoiceDocument document) {
    return Invoice.builder()
      .withId(document.getId())
      .withCnpj(document.getCnpj())
      .withKey(document.getKey())
      .withCoo(document.getCoo())
      .withDate(document.getDate())
      .withValue(document.getValue())
      .withSnapshot(document.getSnapshot())
      .withDatabase(document.getDatabase())
      .build();
  }
}