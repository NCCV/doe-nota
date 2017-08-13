package br.org.nccv.api.couch;

/**
 * Base change for the couch database.
 */
class CouchChange {

  private String revision;

  /**
   * Receive a current change revision.
   *
   * @param revision A specific revision
   */
  protected CouchChange(String revision) {
    this.revision = revision;
  }

  public String getRevision() {
    return revision;
  }
}
