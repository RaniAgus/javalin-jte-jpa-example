package io.github.raniagus.example.config;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.nio.file.Path;
import javax.inject.Named;

public class DevelopmentConfiguration extends Configuration {
  @Provides
  @Named("DB")
  protected Runnable connectToSimplePersistenceUnit() {
    return WithSimplePersistenceUnit::dispose;
  }

  @Provides
  @Singleton
  protected TemplateEngine templateEngine() {
    return TemplateEngine.create(new DirectoryCodeResolver(Path.of("src","main", "jte")), ContentType.Html);
  }

  @Provides
  @Singleton
  @Named("PORT")
  protected Integer port() {
    return 8080;
  }
}
