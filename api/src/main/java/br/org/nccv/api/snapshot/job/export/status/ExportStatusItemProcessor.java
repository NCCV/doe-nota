package br.org.nccv.api.snapshot.job.export.status;

import br.org.nccv.api.couch.CouchService;
import br.org.nccv.api.couch.DatabaseDocument;
import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Retrieve a invoice document from couch database.
 */
@Component
public class ExportStatusItemProcessor implements ItemProcessor<Invoice, DatabaseDocument> {

  @Autowired
  private CouchService couchService;

  /**
   * Transform a invoice into a database document, retrieving it from
   * remote couch database and change the status to sent.
   *
   * @param invoice Database invoice
   * @return Invoice document
   */
  @Override
  public DatabaseDocument process(Invoice invoice) {
    DatabaseDocument databaseDocument = couchService.get(invoice.getDatabase(), invoice.getId());
    return databaseDocument.update("$.status", "sent");
  }
}