package br.org.nccv.api.snapshot;

import org.mapstruct.Mapper;

@Mapper
public interface SnapshotMapper {

  SnapshotResource from(Snapshot snapshot);

}
