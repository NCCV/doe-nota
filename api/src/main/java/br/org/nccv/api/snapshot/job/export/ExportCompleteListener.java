package br.org.nccv.api.snapshot.job.export;

import br.org.nccv.api.snapshot.ExportService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener that process export completeness.
 */
@Component
public class ExportCompleteListener extends JobExecutionListenerSupport {

  @Autowired
  private ExportService exportService;

  /**
   * Execute after the job finishes, saving the success or
   * the fail of the export.
   *
   * @param jobExecution Job execution data
   */
  @Override
  public void afterJob(JobExecution jobExecution) {
    JobParameters jobParameters = jobExecution.getJobParameters();
    BatchStatus status = jobExecution.getStatus();
    String snapshot = jobParameters.getString("snapshot");

    if (status.equals(BatchStatus.COMPLETED)) {
      exportService.finish(snapshot);
    } else if (status.isGreaterThan(BatchStatus.STOPPING)) {
      exportService.fail(snapshot);
    }
  }
}