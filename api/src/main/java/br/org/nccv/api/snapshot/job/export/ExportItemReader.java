package br.org.nccv.api.snapshot.job.export;

import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;

/**
 * Responsible for read paginated data from database.
 */
@Component
@StepScope
public class ExportItemReader extends JpaPagingItemReader<Invoice> {

  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @Value("#{jobParameters['snapshot']}")
  private String snapshot;

  @PostConstruct
  public void initialize() {
    setEntityManagerFactory(entityManagerFactory);
    setParameterValues(Collections.singletonMap("snapshot", snapshot));
    setQueryString(queryString());
  }

  /**
   * Default query string to read invoices.
   *
   * @return The default query string
   */
  protected String queryString() {
    return "select i from Invoice i where i.snapshot = :snapshot";
  }
}
