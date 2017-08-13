package br.org.nccv.api.snapshot;

import br.org.nccv.api.support.Jobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Responsible for create snapshot jobs.
 */
@Service
public class SnapshotJobLauncher {

  private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotService.class);

  @Autowired
  @Qualifier(Jobs.SNAPSHOT)
  private Job snapshotJob;

  @Autowired
  @Qualifier(Jobs.EXPORT)
  private Job exportJob;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private SnapshotService snapshotService;

  @Autowired
  private ExportService exportService;

  /**
   * Creates a new snapshot and create the import snapshotJob for the
   * created snapshot. Every snapshotJob must have a snapshot
   * attached to itself.
   */
  public void create() {
    try {
      Snapshot snapshot = snapshotService.create();
      JobParameters parameters = new JobParametersBuilder()
        .addString("snapshot", snapshot.getId())
        .toJobParameters();

      jobLauncher.run(snapshotJob, parameters);
    } catch (JobExecutionException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }

  /**
   * Export the file for the passed snapshot.
   *
   * @param snapshotId ID of snapshot to startExport the file
   */
  public void export(String snapshotId) {
    try {
      Snapshot snapshot = exportService.export(snapshotId);
      JobParameters parameters = new JobParametersBuilder()
        .addString("snapshot", snapshot.getId())
        .toJobParameters();

      jobLauncher.run(exportJob, parameters);
    } catch (JobExecutionException ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
  }
}
