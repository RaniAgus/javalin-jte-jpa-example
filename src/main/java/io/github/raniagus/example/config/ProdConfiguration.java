package io.github.raniagus.example.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.nio.file.Path;

public class ProdConfiguration extends Configuration {
  protected ProdConfiguration() {
    super();
  }

  @Override
  protected void connectToSimplePersistenceUnit() {
    WithSimplePersistenceUnit.configure(properties -> properties
        .set("hibernate.connection.url", getEnv("DB_URL").orElseThrow())
        .set("hibernate.connection.username", getEnv("DB_USERNAME").orElseThrow())
        .set("hibernate.connection.password", getEnv("DB_PASSWORD").orElseThrow())
        .set("hibernate.hbm2ddl.auto", "validate"));
  }

  @Override
  protected TemplateEngine templateEngine() {
    return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
  }

  @Override
  protected Integer port() {
    return getEnv("PORT").map(Integer::parseInt).orElseThrow();
  }
}
