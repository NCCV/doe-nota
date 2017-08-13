package br.org.nccv.api.configuration;

import br.org.nccv.api.Application;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@EntityScan(basePackageClasses = {
  Application.class, Jsr310JpaConverters.class
})
public class DatabaseConfiguration {
}
