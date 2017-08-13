package br.org.nccv.api.snapshot;

import br.org.nccv.api.exception.BadRequestException;
import br.org.nccv.api.exception.InternalServerException;
import br.org.nccv.api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Export rules.
 */
@Service
public class ExportService {

  @Autowired
  private SnapshotRepository snapshotRepository;

  @Value("#{environment['app.csv.directory']}")
  private String csvDirectory;

  /**
   * Mark a snapshot as exporting using serializable isolation, avoiding
   * snapshot for being exported more than once.
   *
   * @param snapshotId Snapshot ID to be exported
   * @return Exported snapshot
   */
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Snapshot export(String snapshotId) {
    Snapshot snapshot = snapshotRepository.findOne(snapshotId)
      .orElseThrow(() -> NotFoundException.fromCode("snapshot_not_found"));

    if (snapshot.cannotExport()) {
      throw BadRequestException.fromCode("snapshot_was_already_exported");
    }

    snapshot.export();
    snapshotRepository.save(snapshot);

    return snapshot;
  }

  /**
   * Update the snapshot as exported.
   *
   * @param snapshotId Snapshot id
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void finish(String snapshotId) {
    Snapshot snapshot = snapshotRepository.findOne(snapshotId)
      .orElseThrow(() -> NotFoundException.fromCode("snapshot_not_found"));

    snapshot.finishExport();
    snapshotRepository.save(snapshot);
  }

  /**
   * Update snapshot as failed export.
   *
   * @param snapshotId Snapshot id
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void fail(String snapshotId) {
    Snapshot snapshot = snapshotRepository.findOne(snapshotId)
      .orElseThrow(() -> NotFoundException.fromCode("snapshot_not_found"));

    snapshot.failExport();
    snapshotRepository.save(snapshot);
  }

  /**
   * Find a CSV resource previously exported.
   *
   *
   * @param snapshotId Snapshot id
   * @param qualifier Type of file
   * @return CSV file resource
   */
  public String findCsv(String snapshotId, String qualifier) {
    String filename = qualifier.concat("-")
      .concat(snapshotId)
      .concat(".csv");

    Path path = Paths.get(csvDirectory, filename);

    if (!Files.exists(path)) {
      throw NotFoundException.fromCode("snapshot_not_found");
    }

    try {
      return Files.readAllLines(path, Charset.forName("UTF-8"))
        .stream().collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw InternalServerException.fromCode("snapshot_fails_to_process");
    }
  }
}
