package io.github.raniagus.example.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.nio.file.Path;

public class DevelopmentConfiguration extends Configuration {

  protected DevelopmentConfiguration() {
    super();
  }

  @Override
  protected void connectToSimplePersistenceUnit() {
    WithSimplePersistenceUnit.dispose();
  }

  @Override
  protected TemplateEngine templateEngine() {
    return TemplateEngine.create(new DirectoryCodeResolver(Path.of("src","main", "jte")), ContentType.Html);
  }

  @Override
  protected Integer port() {
    return 8080;
  }
}
