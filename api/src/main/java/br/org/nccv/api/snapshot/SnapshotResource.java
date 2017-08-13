package br.org.nccv.api.snapshot;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SnapshotResource {

  @JsonProperty("id")
  private String id;

  @JsonProperty("status")
  private SnapshotStatus status;

  @JsonProperty("createdAt")
  private LocalDateTime createdAt;

  @JsonProperty("itemCount")
  private Integer itemCount;

  @JsonProperty("itemTotal")
  private BigDecimal itemTotal;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SnapshotStatus getStatus() {
    return status;
  }

  public void setStatus(SnapshotStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getItemCount() {
    return itemCount;
  }

  public void setItemCount(Integer itemCount) {
    this.itemCount = itemCount;
  }

  public BigDecimal getItemTotal() {
    return itemTotal;
  }

  public void setItemTotal(BigDecimal itemTotal) {
    this.itemTotal = itemTotal;
  }
}
