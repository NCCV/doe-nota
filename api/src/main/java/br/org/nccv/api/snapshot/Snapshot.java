package br.org.nccv.api.snapshot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NamedQuery(
  name = "Snapshot.getLastOne",
  query = "select se from Snapshot se where se.createdAt = (select max(si.createdAt) from Snapshot si)"
)
@Table(name = "snapshot")
public class Snapshot {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private SnapshotStatus status;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "item_count")
  private Integer itemCount;

  @Column(name = "item_total")
  private BigDecimal itemTotal;

  protected Snapshot() {
  }

  private Snapshot(String id) {
    this.id = id;
    this.status = SnapshotStatus.STARTED;
    this.createdAt = LocalDateTime.now();
    this.itemCount = 0;
    this.itemTotal = BigDecimal.ZERO;
  }

  protected static Snapshot of() {
    return new Snapshot(String.valueOf(UUID.randomUUID()));
  }

  public String getId() {
    return id;
  }

  public SnapshotStatus getStatus() {
    return status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Integer getItemCount() {
    return itemCount;
  }

  public BigDecimal getItemTotal() {
    return itemTotal;
  }

  /**
   * Completes the snapshot.
   *
   * @param itemCount Items count from snapshot
   * @param itemTotal
   */
  protected void complete(int itemCount, BigDecimal itemTotal) {
    this.itemCount = itemCount;
    this.itemTotal = itemTotal;
    this.status = SnapshotStatus.COMPLETED;
  }

  /**
   * Fail the snapshot.
   */
  protected void fail() {
    this.status = SnapshotStatus.FAILED;
  }

  /**
   * Initialize the exportation.
   */
  protected void export() {
    this.status = SnapshotStatus.EXPORTING;
  }

  /**
   * Finish the export
   */
  protected void finishExport() {
    this.status = SnapshotStatus.EXPORTED;
  }

  /**
   * Fails the export.
   */
  protected void failExport() {
    this.status = SnapshotStatus.EXPORT_FAILED;
  }

  /**
   * Check if the snapshot was started.
   *
   * @return true if is started, false otherwise
   */
  protected boolean isStarted() {
    return SnapshotStatus.STARTED.equals(status);
  }

  /**
   * Check if the snapshot can't be exported.
   *
   * @return true if can't be exported, false otherwise
   */
  protected boolean cannotExport() {
    return SnapshotStatus.EXPORTING.equals(status)
      || SnapshotStatus.EXPORTED.equals(status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, status, createdAt);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Snapshot) {
      Snapshot that = (Snapshot) other;
      return Objects.equals(id, that.id)
        && Objects.equals(status, that.status)
        && Objects.equals(createdAt, that.createdAt)
        && Objects.equals(itemCount, that.itemCount);
    }

    return false;
  }
}
