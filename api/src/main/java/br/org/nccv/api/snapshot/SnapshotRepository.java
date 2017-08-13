package br.org.nccv.api.snapshot;

import br.org.nccv.api.support.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Snapshot}.
 */
@Repository
public interface SnapshotRepository extends BaseRepository<Snapshot, String> {

  /**
   * Get the last snapshot created.
   */
  Optional<Snapshot> getLastOne();

}
