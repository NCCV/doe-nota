package br.org.nccv.api.snapshot.job.snapshot;

import br.org.nccv.api.invoice.Invoice;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

/**
 * Responsible for write {@link Invoice} to the database.
 */
@Component
public class SnapshotItemWriter extends JpaItemWriter<Invoice> {

  @Autowired
  public SnapshotItemWriter(EntityManagerFactory entityManagerFactory) {
    setEntityManagerFactory(entityManagerFactory);
  }

}
