package br.org.nccv.api.invoice;

import br.org.nccv.api.couch.CouchService;
import br.org.nccv.api.couch.DatabaseChange;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Invoice document service rules.
 */
@Service
public class InvoiceDocumentService {

  @Autowired
  private CouchService couchService;

  /**
   * Find database changes for database since the last local version.
   *
   * @param database Database to find changes
   * @param version  Last local change
   * @return Database changes
   */
  public DatabaseChange<InvoiceDocument> findChanges(String database, String version) {
    ResponseEntity<String> responseEntity = couchService.findChanges(database, version);
    DocumentContext document = JsonPath.parse(responseEntity.getBody());

    String revision = document.read("$.last_seq", String.class);
    List<InvoiceDocument> databases = document.read("$.results[*].doc", new TypeRef<List<InvoiceDocument>>() {});

    return new DatabaseChange<>(revision, databases);
  }
}
