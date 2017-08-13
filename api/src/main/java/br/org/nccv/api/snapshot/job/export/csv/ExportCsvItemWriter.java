package br.org.nccv.api.snapshot.job.export.csv;

import br.org.nccv.api.invoice.Invoice;
import br.org.nccv.api.invoice.InvoiceCsv;
import br.org.nccv.api.support.batch.CsvLineAggregator;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;

/**
 * Responsible for write {@link Invoice} to the CSV file.
 */
public abstract class ExportCsvItemWriter extends FlatFileItemWriter<InvoiceCsv> {

  @Value("#{jobParameters['snapshot']}")
  private String snapshot;

  @Value("#{environment['app.csv.directory']}")
  private String csvDirectory;

  @PostConstruct
  public void initialize() {
    String filename = qualifier().concat("-").concat(snapshot);

    BeanWrapperFieldExtractor<InvoiceCsv> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(fields());

    CsvLineAggregator<InvoiceCsv> lineAggregator = new CsvLineAggregator<>();
    lineAggregator.setFieldExtractor(fieldExtractor);

    setEncoding("UTF-8");
    setLineAggregator(lineAggregator);
    setResource(new PathResource(Paths.get(csvDirectory, filename.concat(".csv"))));
    setHeaderCallback(writer -> writer.write(header()));
  }

  /**
   * Get the qualifier type for the csv file.
   *
   * @return Qualifier name
   */
  protected abstract String qualifier();

  /**
   * Get the fields to read from bean.
   *
   * @return The array with fields
   */
  protected abstract String[] fields();

  /**
   * Return the heade for the fields.
   *
   * @return Header for the fields.
   */
  protected abstract String header();
}
