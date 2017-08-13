package br.org.nccv.api.snapshot;

import br.org.nccv.api.exception.BadRequestException;
import br.org.nccv.api.revision.Revision;
import br.org.nccv.api.revision.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static br.org.nccv.api.snapshot.SnapshotSpecifications.excludingZeros;

/**
 * Snapshot rules.
 */
@Service
public class SnapshotService {

  @Autowired
  private SnapshotRepository snapshotRepository;

  @Autowired
  private RevisionService revisionService;

  /**
   * Creates a snapshot using serializable isolation, avoiding
   * snapshot for being created concurrently.
   *
   * @return a new {@link Snapshot}
   * @throws BadRequestException if any snapshot is already running
   */
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Snapshot create() {
    Optional<Snapshot> snapshotOptional = snapshotRepository.getLastOne();

    if (snapshotOptional.isPresent() && snapshotOptional.get().isStarted()) {
      throw BadRequestException.fromCode("snapshot_already_started");
    }

    return snapshotRepository.save(Snapshot.of());
  }

  /**
   * Update the snapshot as completed and save revisions used
   * within the process.
   *
   * @param snapshotId Snapshot id
   * @param revisions  Last revisions from couchdb
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void complete(String snapshotId, List<Revision> revisions, int itemCount, BigDecimal itemTotal) {
    Snapshot snapshot = snapshotRepository.findOne(snapshotId)
      .orElseThrow(() -> new IllegalArgumentException("Snapshot not found"));

    snapshot.complete(itemCount, itemTotal);
    snapshotRepository.save(snapshot);
    revisionService.update(revisions);
  }

  /**
   * Update snapshot as failed.
   *
   * @param snapshotId Snapshot id
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void fail(String snapshotId) {
    Snapshot snapshot = snapshotRepository.findOne(snapshotId)
      .orElseThrow(() -> new IllegalArgumentException("Snapshot not found"));

    snapshot.fail();
    snapshotRepository.save(snapshot);
  }

  /**
   * Find all invoices paginated.
   *
   * @param pageable Paging request
   * @return Paged result
   */
  @Transactional(readOnly = true)
  public Page<Snapshot> findAll(Pageable pageable) {
    return snapshotRepository.findAll(excludingZeros(), pageable);
  }
}
