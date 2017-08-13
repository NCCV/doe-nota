package br.org.nccv.api.snapshot.job.export;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

/**
 * Responsible for read paginated data from database.
 */
@Component
@StepScope
public class ExportKeyItemReader extends ExportItemReader {

  /**
   * Select only invoices with not empty keys.
   *
   * @return Query string
   */
  protected String queryString() {
    return "select i from Invoice i where i.snapshot = :snapshot and key <> ''";
  }

}
