package br.org.nccv.api.snapshot.job.export;

import br.org.nccv.api.couch.DatabaseDocument;
import br.org.nccv.api.invoice.Invoice;
import br.org.nccv.api.invoice.InvoiceCsv;
import br.org.nccv.api.snapshot.job.export.csv.ExportCsvCooItemWriter;
import br.org.nccv.api.snapshot.job.export.csv.ExportCsvItemProcessor;
import br.org.nccv.api.snapshot.job.export.csv.ExportCsvItemWriter;
import br.org.nccv.api.snapshot.job.export.csv.ExportCsvKeyItemWriter;
import br.org.nccv.api.snapshot.job.export.status.ExportStatusItemProcessor;
import br.org.nccv.api.snapshot.job.export.status.ExportStatusItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialRandomBackOffPolicy;

/**
 * Configure the export job with readers, processors and writers.
 */
@Configuration
public class ExportBatchConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ExportCompleteListener exportCompleteListener;

  @Autowired
  private ExportItemReader exportItemReader;

  @Autowired
  private ExportKeyItemReader exportKeyItemReader;

  @Autowired
  private ExportCsvKeyItemWriter exportCsvKeyItemWriter;

  @Autowired
  private ExportCooItemReader exportCooItemReader;

  @Autowired
  private ExportCsvCooItemWriter exportCsvCooItemWriter;

  @Autowired
  private ExportCsvItemProcessor exportCsvItemProcessor;

  @Autowired
  private ExportStatusItemProcessor exportStatusItemProcessor;

  @Autowired
  private ExportStatusItemWriter exportStatusItemWriter;

  /**
   * Export invoice job.
   */
  @Bean
  public Job exportJob() {
    return jobBuilderFactory.get("exportJob")
      .incrementer(new RunIdIncrementer())
      .listener(exportCompleteListener)
      .start(exportKeyStep())
      .next(exportCooStep())
      .next(exportStatusStep())
      .build();
  }

  /**
   * Export invoice step.
   */
  @Bean
  public Step exportKeyStep() {
    return stepBuilderFactory.get("exportKeyStep")
      .<Invoice, InvoiceCsv>chunk(20)
      .reader(exportKeyItemReader)
      .processor(exportCsvItemProcessor)
      .writer(exportCsvKeyItemWriter)
      .build();
  }

  /**
   * Export invoice step.
   */
  @Bean
  public Step exportCooStep() {
    return stepBuilderFactory.get("exportCooStep")
      .<Invoice, InvoiceCsv>chunk(20)
      .reader(exportCooItemReader)
      .processor(exportCsvItemProcessor)
      .writer(exportCsvCooItemWriter)
      .build();
  }

  /**
   * Export invoice status step.
   */
  @Bean
  public Step exportStatusStep() {
    return stepBuilderFactory.get("exportStatusStep")
      .<Invoice, DatabaseDocument>chunk(20)
      .faultTolerant()
      .backOffPolicy(new ExponentialRandomBackOffPolicy())
      .reader(exportItemReader)
      .processor(exportStatusItemProcessor)
      .writer(exportStatusItemWriter)
      .build();
  }
}
