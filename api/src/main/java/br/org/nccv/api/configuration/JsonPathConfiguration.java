package br.org.nccv.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration.Defaults;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.EnumSet;
import java.util.Set;

@Configuration
public class JsonPathConfiguration {

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  public void initialize() {
    com.jayway.jsonpath.Configuration.setDefaults(new Defaults() {
      private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
      private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

      @Override
      public JsonProvider jsonProvider() {
        return jsonProvider;
      }

      @Override
      public MappingProvider mappingProvider() {
        return mappingProvider;
      }

      @Override
      public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
      }
    });
  }

}
