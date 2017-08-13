package br.org.nccv.api.snapshot.job.snapshot;

import br.org.nccv.api.invoice.Invoice;
import br.org.nccv.api.invoice.InvoiceDocument;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the snapshot job with readers, processors and writers.
 */
@Configuration
public class SnapshotBatchConfiguration {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private SnapshotCompleteListener snapshotCompleteListener;

  @Autowired
  private SnapshotItemReader snapshotItemReader;

  @Autowired
  private SnapshotItemProcessor snapshotItemProcessor;

  @Autowired
  private SnapshotItemWriter snapshotItemWriter;

  /**
   * Snapshot invoice job.
   */
  @Bean
  public Job snapshotJob() {
    return jobBuilderFactory.get("snapshotJob")
      .incrementer(new RunIdIncrementer())
      .listener(snapshotCompleteListener)
      .start(snapshotStep())
      .build();
  }

  /**
   * Snapshot invoice step.
   */
  @Bean
  public Step snapshotStep() {
    return stepBuilderFactory.get("snapshotStep")
      .allowStartIfComplete(true)
      .<InvoiceDocument, Invoice>chunk(20)
      .reader(snapshotItemReader)
      .processor(snapshotItemProcessor)
      .writer(snapshotItemWriter)
      .build();
  }
}
