package br.org.nccv.api.snapshot.job.export.status;

import br.org.nccv.api.couch.CouchService;
import br.org.nccv.api.couch.DatabaseDocument;
import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Responsible for write {@link Invoice} to the database.
 */
@Component
public class ExportStatusItemWriter implements ItemWriter<DatabaseDocument> {

  @Autowired
  private CouchService couchService;

  @Override
  public void write(List<? extends DatabaseDocument> items) {
    Map<String, List<DatabaseDocument>> databases = items.stream()
      .collect(Collectors.groupingBy(DatabaseDocument::getDatabase, Collectors.toList()));

    for (Map.Entry<String, List<DatabaseDocument>> current : databases.entrySet()) {
      couchService.post(current.getKey(), current.getValue());
    }
  }
}
