package br.org.nccv.api.snapshot.job.export.csv;

import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

/**
 * Responsible for write {@link Invoice} to the CSV file.
 */
@Component
@StepScope
public class ExportCsvKeyItemWriter extends ExportCsvItemWriter {

  /**
   * Default invoice type qualifier.
   *
   * @return Invoice qualifier
   */
  @Override
  protected String qualifier() {
    return "key";
  }

  /**
   * Default invoice fields.
   *
   * @return Invoice fields
   */
  @Override
  protected String[] fields() {
    return new String[]{"cnpj", "date", "key", "value", "type"};
  }

  /**
   * Default invoice header.
   *
   * @return Invoice header
   */
  @Override
  protected String header() {
    return "CNPJ,Data,Codigo,Valor,CFouNF";
  }
}
