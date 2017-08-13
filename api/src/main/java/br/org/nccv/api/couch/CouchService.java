package br.org.nccv.api.couch;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Couchdb database changes service.
 */
@Service
public class CouchService {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private CouchRequestSupport couchRequestSupport;

  /**
   * Find global changes from couchdb server.
   *
   * @param version Last local revision
   * @return Global changes from server
   */
  public GlobalChange findChanges(String version) {
    URI uri = couchRequestSupport.startUri("_db_updates")
      .queryParam("since", version)
      .build().toUri();

    RequestEntity requestEntity = couchRequestSupport.authorize(RequestEntity.get(uri)).build();
    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

    DocumentContext document = JsonPath.parse(responseEntity.getBody());
    String revision = document.read("$.last_seq", String.class);
    Set<String> databases = document.read("$.results[*].db_name", new TypeRef<Set<String>>() {});

    return new GlobalChange(revision, databases);
  }

  /**
   * Find changes from database and return response entity.
   *
   * @param database Database to check
   * @param version  Last local revision
   * @return Response entity with body
   */
  public ResponseEntity<String> findChanges(String database, String version) {
    URI uri = couchRequestSupport.startUri(database, "_changes")
      .queryParam("include_docs", "true")
      .queryParam("since", version)
      .queryParam("filter", "_view")
      .queryParam("view", "invoice/scanned")
      .build().toUri();

    RequestEntity requestEntity = couchRequestSupport.authorize(RequestEntity.get(uri)).build();
    return restTemplate.exchange(requestEntity, String.class);
  }

  /**
   * Find one document from the database and return response entity.
   *
   * @param database Database to check
   * @param id       Document ID
   * @return Remote database document
   */
  public DatabaseDocument get(String database, String id) {
    URI uri = couchRequestSupport.startUri(database, id).build().toUri();
    RequestEntity requestEntity = couchRequestSupport.authorize(RequestEntity.get(uri)).build();
    ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

    return new DatabaseDocument(database, id, responseEntity.getBody());
  }

  /**
   * Post all documents to database using bulk
   *
   * @param database  Database to post
   * @param documents Documents to post to database
   */
  public void post(String database, List<DatabaseDocument> documents) {
    String joinedDocuments = documents.stream()
      .map(DatabaseDocument::getBody)
      .collect(Collectors.joining(","));

    URI uri = couchRequestSupport.startUri(database, "_bulk_docs").build().toUri();
    RequestEntity requestEntity = couchRequestSupport.authorize(RequestEntity.post(uri))
      .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body("{\"docs\": [".concat(joinedDocuments).concat("]}"));

    restTemplate.exchange(requestEntity, String.class);
  }
}
