package br.org.nccv.api.couch;

import com.jayway.jsonpath.JsonPath;

/**
 * Represents a dynamic couch database document.
 */
public class DatabaseDocument {

  private String database;
  private String id;
  private String body;

  /**
   * Create a new database document representation.
   *
   * @param database Database reference
   * @param id       Document id
   * @param body     Document representation text
   */
  public DatabaseDocument(String database, String id, String body) {
    this.database = database;
    this.id = id;
    this.body = body;
  }

  public String getDatabase() {
    return database;
  }

  public String getId() {
    return id;
  }

  public String getBody() {
    return body;
  }

  /**
   * Update the document body using a json path.
   *
   * @param path  Json path to be applied
   * @param value Value to be applied
   * @return Updated database document
   */
  public DatabaseDocument update(String path, String value) {
    this.body = JsonPath.parse(this.body)
      .set(path, value)
      .jsonString();

    return this;
  }
}
