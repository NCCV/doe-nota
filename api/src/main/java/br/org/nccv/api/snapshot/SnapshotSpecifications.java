package br.org.nccv.api.snapshot;


import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for quering {@link Snapshot}.
 */
public class SnapshotSpecifications {

  public static Specification<Snapshot> excludingZeros() {
    return (root, query, builder) -> builder.greaterThan(root.get(Snapshot_.itemCount), 0);
  }

  private SnapshotSpecifications() {
  }

}
