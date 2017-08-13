package br.org.nccv.api.revision;

import br.org.nccv.api.support.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Revision}.
 */
@Repository
public interface RevisionRepository extends BaseRepository<Revision, String> {
}
