package br.org.nccv.api.couch;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Maintain track from the couch global changes.
 */
public class GlobalChange extends CouchChange {

  private Set<String> databases = new HashSet<>();

  /**
   * Create a new global change.
   *
   * @param revision  Current global revision
   * @param databases Changed databases
   */
  public GlobalChange(String revision, Set<String> databases) {
    super(revision);
    this.databases.addAll(databases);
  }

  public Set<String> getDatabases() {
    return Collections.unmodifiableSet(databases);
  }
}
