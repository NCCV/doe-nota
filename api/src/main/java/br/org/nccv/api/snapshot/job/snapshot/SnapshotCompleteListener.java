package br.org.nccv.api.snapshot.job.snapshot;

import br.org.nccv.api.revision.Revision;
import br.org.nccv.api.snapshot.SnapshotService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SnapshotCompleteListener extends JobExecutionListenerSupport {

  @Autowired
  private SnapshotService snapshotService;

  /**
   * Execute after the job finishes, saving the success or
   * the fail of the snapshot.
   *
   * @param jobExecution Job execution data
   */
  @Override
  public void afterJob(JobExecution jobExecution) {
    JobParameters jobParameters = jobExecution.getJobParameters();
    BatchStatus status = jobExecution.getStatus();
    String snapshot = jobParameters.getString("snapshot");

    if (status.equals(BatchStatus.COMPLETED)) {
      processSuccess(jobExecution, snapshot);
    } else if (status.isGreaterThan(BatchStatus.STOPPING)) {
      snapshotService.fail(snapshot);
    }
  }

  /**
   * Process the success of the snapshot creation, saving the revisions
   * and updating the snapshot with job id.
   *
   * @param snapshot     Snapshot executed
   * @param jobExecution Job execution data
   */
  @SuppressWarnings("unchecked")
  private void processSuccess(JobExecution jobExecution, String snapshot) {
    ExecutionContext executionContext = jobExecution.getExecutionContext();
    List<Revision> revisions = (List<Revision>) executionContext.get("revisions");
    int itemCount = executionContext.getInt("itemCount");
    BigDecimal itemTotal = (BigDecimal) executionContext.get("itemTotal");

    snapshotService.complete(snapshot, revisions, itemCount, itemTotal);
  }
}