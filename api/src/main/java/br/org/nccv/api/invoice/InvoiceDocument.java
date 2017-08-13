package br.org.nccv.api.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceDocument {

  @JsonProperty("_id")
  private String id;

  @JsonProperty("cnpj")
  private String cnpj;

  @JsonProperty("key")
  private String key;

  @JsonProperty("coo")
  private String coo;

  @JsonProperty("date")
  private LocalDateTime date;

  @JsonProperty("value")
  private BigDecimal value;

  @JsonIgnore
  private String snapshot;

  @JsonIgnore
  private String database;

  protected InvoiceDocument() {
    this.id = "00000000-0000-0000-0000-000000000000";
  }

  public String getId() {
    return id;
  }

  public String getCnpj() {
    return MoreObjects.firstNonNull(cnpj, "");
  }

  public String getKey() {
    return MoreObjects.firstNonNull(key, "");
  }

  public String getCoo() {
    return MoreObjects.firstNonNull(coo, "");
  }

  public LocalDateTime getDate() {
    return date;
  }

  public BigDecimal getValue() {
    return value;
  }

  public String getSnapshot() {
    return snapshot;
  }

  public String getDatabase() {
    return database;
  }

  /**
   * Assign the created snapshot.
   *
   * @param snapshot In progress snapshot
   */
  public void assignSnapshot(String snapshot) {
    this.snapshot = snapshot;
  }

  /**
   * Assign the current database.
   *
   * @param database Current database
   */
  public void assignDatabase(String database) {
    this.database = database;
  }
}
