package br.org.nccv.api.snapshot.job.export.csv;

import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

/**
 * Responsible for write {@link Invoice} to the CSV file.
 */
@Component
@StepScope
public class ExportCsvCooItemWriter extends ExportCsvItemWriter {

  /**
   * Default invoice type qualifier.
   *
   * @return Invoice qualifier
   */
  @Override
  protected String qualifier() {
    return "coo";
  }

  /**
   * Default invoice fields.
   *
   * @return Invoice fields
   */
  @Override
  protected String[] fields() {
    return new String[]{"cnpj", "date", "coo", "value", "type"};
  }

  /**
   * Default invoice header.
   *
   * @return Invoice header
   */
  @Override
  protected String header() {
    return "CNPJ,Data,COO,Valor,CFouNF";
  }
}
