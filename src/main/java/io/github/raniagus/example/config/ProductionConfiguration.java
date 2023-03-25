package io.github.raniagus.example.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.nio.file.Path;

public class ProductionConfiguration extends Configuration {

  protected ProductionConfiguration() {
    super();
  }

  @Override
  protected void connectToSimplePersistenceUnit() {
    WithSimplePersistenceUnit.configure(properties -> properties
        .set("hibernate.connection.url", getRequiredEnv("DB_URL"))
        .set("hibernate.connection.username", getRequiredEnv("DB_USERNAME"))
        .set("hibernate.connection.password", getRequiredEnv("DB_PASSWORD"))
        .set("hibernate.hbm2ddl.auto", "validate"));
  }

  @Override
  protected TemplateEngine templateEngine() {
    return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
  }

  @Override
  protected Integer port() {
    return Integer.parseInt(getRequiredEnv("PORT"));
  }
}
