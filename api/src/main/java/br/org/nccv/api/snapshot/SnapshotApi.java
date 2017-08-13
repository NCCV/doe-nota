package br.org.nccv.api.snapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Api to work with snapshots.
 */
@RestController
@RequestMapping("/snapshots")
public class SnapshotApi {

  @Autowired
  private SnapshotMapper snapshotMapper;

  @Autowired
  private SnapshotService snapshotService;

  @Autowired
  private ExportService exportService;

  @Autowired
  private SnapshotJobLauncher snapshotJobLauncher;

  /**
   * Create a new snapshot.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void post() {
    snapshotJobLauncher.create();
  }

  /**
   * Return a csv file previously exported.
   *
   * @param id Snapshot id
   * @return CSV file
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "{id}", produces = "text/csv")
  public String get(@PathVariable String id, @RequestParam String qualifier) {
    return exportService.findCsv(id, qualifier);
  }

  /**
   * Find all snapshots available.
   *
   * @param pageable Page request
   * @return Paged snapshot resources
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<SnapshotResource> get(Pageable pageable) {
    Page<Snapshot> page = snapshotService.findAll(pageable);
    return page.map(snapshotMapper::from);
  }
}
