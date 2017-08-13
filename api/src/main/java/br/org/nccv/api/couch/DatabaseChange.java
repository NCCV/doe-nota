package br.org.nccv.api.couch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Maintain track from the couch database changes.
 */
public class DatabaseChange<T> extends CouchChange {

  private List<T> documents = new ArrayList<>();

  /**
   * Create a new database change.
   *
   * @param revision  Current database revision
   * @param documents Changed documents
   */
  public DatabaseChange(String revision, List<T> documents) {
    super(revision);
    this.documents.addAll(documents);
  }

  public boolean isEmpty() {
    return documents.isEmpty();
  }

  public List<T> getDocuments() {
    return Collections.unmodifiableList(documents);
  }

  public int getCount() {
    return documents.size();
  }
}
