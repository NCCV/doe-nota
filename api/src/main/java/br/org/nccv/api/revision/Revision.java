package br.org.nccv.api.revision;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "revision")
public class Revision {

  public static final String GLOBAL = "global";
  public static final String FIRST = "0";

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "version")
  private String version;

  protected Revision() {
  }

  private Revision(String id, String version) {
    this.id = id;
    this.version = version;
  }

  public static Revision of(String id, String version) {
    return new Revision(id, version);
  }

  public String getId() {
    return id;
  }

  public String getVersion() {
    return version;
  }

  public void update(String version) {
    this.version = version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Revision) {
      Revision that = (Revision) other;
      return Objects.equals(id, that.id)
        && Objects.equals(version, that.version);
    }

    return false;
  }
}
