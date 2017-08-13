package br.org.nccv.api.invoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "invoice")
public class Invoice {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "cl_cnpj")
  private String cnpj;

  @Column(name = "cl_key")
  private String key;

  @Column(name = "cl_coo")
  private String coo;

  @Column(name = "cl_date")
  private LocalDateTime date;

  @Column(name = "cl_value")
  private BigDecimal value;

  @Column(name = "cl_snapshot")
  private String snapshot;

  @Column(name = "cl_database")
  private String database;

  protected Invoice() {
  }

  private Invoice(Builder builder) {
    this.id = builder.id;
    this.cnpj = builder.cnpj;
    this.key = builder.key;
    this.coo = builder.coo;
    this.date = builder.date;
    this.value = builder.value;
    this.snapshot = builder.snapshot;
    this.database = builder.database;
  }

  public String getId() {
    return id;
  }

  public String getCnpj() {
    return cnpj;
  }

  public String getKey() {
    return key;
  }

  public String getCoo() {
    return coo;
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

  @Override
  public int hashCode() {
    return Objects.hash(id, database);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Invoice) {
      Invoice that = (Invoice) other;
      return Objects.equals(id, that.id)
        && Objects.equals(database, that.database);
    }

    return false;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String id;
    private String cnpj = "";
    private String key = "";
    private String coo = "";
    private LocalDateTime date;
    private BigDecimal value;
    private String snapshot;
    private String database;

    public Builder withCnpj(String cnpj) {
      this.cnpj = cnpj;
      return this;
    }

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withCoo(String coo) {
      this.coo = coo;
      return this;
    }

    public Builder withKey(String key) {
      this.key = key;
      return this;
    }

    public Builder withDate(LocalDateTime date) {
      this.date = date;
      return this;
    }

    public Builder withValue(BigDecimal value) {
      this.value = value;
      return this;
    }

    public Builder withDatabase(String database) {
      this.database = database;
      return this;
    }

    public Builder withSnapshot(String snapshot) {
      this.snapshot = snapshot;
      return this;
    }

    public Invoice build() {
      Objects.requireNonNull(id, "ID cannot be null");
      Objects.requireNonNull(cnpj, "CNPJ cannot be null");
      Objects.requireNonNull(key, "Key cannot be null");
      Objects.requireNonNull(coo, "COO cannot be null");
      Objects.requireNonNull(date, "Date cannot be null");
      Objects.requireNonNull(value, "Value cannot be null");
      Objects.requireNonNull(snapshot, "Snapshot cannot be null");
      Objects.requireNonNull(database, "Database cannot be null");

      return new Invoice(this);
    }
  }
}
