package br.org.nccv.api.couch;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

/**
 * Responsible for initialize URIs and authorize the couchdb remote access.
 */
@Component
public class CouchRequestSupport {

  @Value("#{environment['app.couchdb.url']}")
  private String couchUrl;

  @Value("#{environment['app.couchdb.username']}")
  private String username;

  @Value("#{environment['app.couchdb.password']}")
  private String password;

  /**
   * Start a URI from the base couchdb URL and return its builder.
   *
   * @param segments Path segments for the URI
   * @return URI builder
   */
  public UriComponentsBuilder startUri(String... segments) {
    return UriComponentsBuilder.fromHttpUrl(couchUrl)
      .pathSegment(segments);
  }

  /**
   * Add the basic authorization header to the request entity. Generally
   * it is used in get request entities.
   *
   * @param headerBuilder Header builder from request entity
   * @return Header builder with authorization header
   */
  public HeadersBuilder authorize(HeadersBuilder headerBuilder) {
    return headerBuilder.header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader());
  }

  /**
   * Add the basic authorization header to the request entity. Generally
   * it is used in post or put request entities.
   *
   * @param bodyBuilder Body builder from request entity
   * @return Body builder with authorization header
   */
  public BodyBuilder authorize(BodyBuilder bodyBuilder) {
    return bodyBuilder.header(HttpHeaders.AUTHORIZATION, createAuthorizationHeader());
  }

  /**
   * Join username with password and transform it to a base64 value for
   * basic authorization.
   *
   * @return Encoded base64 header value
   */
  private String createAuthorizationHeader() {
    String authorization = username.concat(":").concat(password);
    byte[] authorizationBytes = authorization.getBytes(StandardCharsets.US_ASCII);
    byte[] encodedAuthorization = Base64.encodeBase64(authorizationBytes);

    return "Basic ".concat(new String(encodedAuthorization));
  }

}
