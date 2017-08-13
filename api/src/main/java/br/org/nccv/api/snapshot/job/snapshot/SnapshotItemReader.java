package br.org.nccv.api.snapshot.job.snapshot;

import br.org.nccv.api.couch.CouchService;
import br.org.nccv.api.couch.DatabaseChange;
import br.org.nccv.api.couch.GlobalChange;
import br.org.nccv.api.invoice.InvoiceDocument;
import br.org.nccv.api.invoice.InvoiceDocumentService;
import br.org.nccv.api.revision.Revision;
import br.org.nccv.api.revision.RevisionService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Responsible for get changed document for each couch database
 * and save into SQL database.
 */
@Component
@StepScope
public class SnapshotItemReader extends ItemStreamSupport implements ItemStreamReader<InvoiceDocument> {

  private static final String DATABASE_PREFIX = "nccv-";

  @Autowired
  private CouchService couchService;

  @Autowired
  private RevisionService revisionService;

  @Autowired
  private InvoiceDocumentService invoiceDocumentService;

  @Value("#{jobParameters['snapshot']}")
  private String snapshot;

  private int current = 0;
  private int count = 0;
  private BigDecimal total = BigDecimal.ZERO;
  private Object lock = new Object();
  private Deque<String> databases = new LinkedList<>();
  private List<Revision> revisions = new ArrayList<>();
  private volatile List<InvoiceDocument> results = new CopyOnWriteArrayList<>();

  /**
   * @see ItemStreamReader#open(ExecutionContext)
   */
  @Override
  public void open(ExecutionContext executionContext) {
    Revision globalRevision = revisionService.findOne(Revision.GLOBAL);
    GlobalChange globalChange = couchService.findChanges(globalRevision.getVersion());

    globalChange.getDatabases().stream()
      .filter(o -> o.startsWith(DATABASE_PREFIX))
      .forEach(databases::push);

    globalRevision.update(globalChange.getRevision());
    revisions.add(globalRevision);
  }

  /**
   * Read each item from current database return it into
   * the spring batch flow.
   *
   * @return {@link InvoiceDocument}
   * @see ItemStreamReader#read()
   */
  @Override
  public InvoiceDocument read() {
    synchronized (lock) {
      if (!databases.isEmpty() && current >= results.size()) {
        readPage();
        current = 0;
      }

      int next = current++;

      if (next < results.size()) {
        return results.get(next);
      } else {
        return null;
      }
    }
  }

  /**
   * Read each database who had documents added or removed. Each
   * database is considered a page in this reader.
   */
  private void readPage() {
    String database;
    Revision databaseRevision;
    DatabaseChange<InvoiceDocument> databaseChange;

    do {
      database = databases.pop();
      databaseRevision = revisionService.findOne(database);
      databaseChange = invoiceDocumentService
        .findChanges(database, databaseRevision.getVersion());

    } while (!databases.isEmpty() && databaseChange.isEmpty());

    readPage(database, databaseRevision, databaseChange);
  }

  /**
   * Effectively read the page.
   *
   * @param database       Database to read
   * @param revision       Current database revision
   * @param databaseChange Changed database
   */
  private void readPage(String database, Revision revision, DatabaseChange<InvoiceDocument> databaseChange) {
    for (InvoiceDocument document : databaseChange.getDocuments()) {
      document.assignDatabase(database);
      document.assignSnapshot(snapshot);
    }

    results.clear();
    results.addAll(databaseChange.getDocuments());
    count += databaseChange.getCount();
    total = databaseChange.getDocuments().stream()
      .map(InvoiceDocument::getValue)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    revision.update(databaseChange.getRevision());
    revisions.add(revision);
  }

  /**
   * Save data to be consumed by listeners and next steps.
   *
   * @param stepExecution Current step execution
   */
  @AfterStep
  public void afterStep(StepExecution stepExecution) {
    JobExecution jobExecution = stepExecution.getJobExecution();
    ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
    jobExecutionContext.put("revisions", Collections.unmodifiableList(revisions));
    jobExecutionContext.put("itemCount", count);
    jobExecutionContext.put("itemTotal", total);
  }
}