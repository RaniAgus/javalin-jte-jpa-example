package io.github.raniagus.example.config;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import java.nio.file.Path;

public class DevConfiguration extends Configuration {
  protected DevConfiguration() {
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