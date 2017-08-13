package br.org.nccv.api.snapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Api to work with exports.
 */
@RestController
@RequestMapping("/snapshots/{id}/exports")
public class ExportApi {

  @Autowired
  private SnapshotJobLauncher snapshotJobLauncher;

  /**
   * Export a snapshot.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void post(@PathVariable String id) {
    snapshotJobLauncher.export(id);
  }
}
