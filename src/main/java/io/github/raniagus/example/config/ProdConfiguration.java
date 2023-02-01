package io.github.raniagus.example.config;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import java.nio.file.Path;
import java.util.Optional;

public class ProdConfiguration extends Configuration {
  protected ProdConfiguration() {
    super();
  }

  @Override
  protected void connectToSimplePersistenceUnit() {
    WithSimplePersistenceUnit.configure(properties -> properties
        .set("hibernate.connection.url", System.getenv("DB_URL"))
        .set("hibernate.connection.username", System.getenv("DB_USERNAME"))
        .set("hibernate.connection.password", System.getenv("DB_PASSWORD"))
        .set("hibernate.hbm2ddl.auto", "validate"));
  }

  @Override
  protected TemplateEngine templateEngine() {
    return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
  }

  @Override
  protected Integer port() {
    return Optional.ofNullable(System.getenv("PORT"))
        .map(Integer::parseInt)
        .orElseThrow();
  }
}
