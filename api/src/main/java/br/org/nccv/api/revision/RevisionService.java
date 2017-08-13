package br.org.nccv.api.revision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Revision rules.
 */
@Service
public class RevisionService {

  @Autowired
  private RevisionRepository revisionRepository;

  /**
   * Find one based on the id. If not found, the result is
   * a default global revision with {@link Revision#FIRST}.
   *
   * @param id Revision service
   * @return Persisted revision or new one
   */
  @Transactional
  public Revision findOne(String id) {
    return revisionRepository.findOne(id)
      .orElse(Revision.of(id, Revision.FIRST));
  }

  /**
   * Save all revisions. If any revision no exists, a new one will be
   * saved. If exists, the revision will be updated.
   *
   * It's important point out that this method requires a started transaction.
   *
   * @param revisions Revisions to be saved
   */
  @Transactional(propagation = Propagation.SUPPORTS)
  public void update(List<Revision> revisions) {
    revisionRepository.save(revisions);
  }
}
