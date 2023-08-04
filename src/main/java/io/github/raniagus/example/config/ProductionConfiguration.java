package io.github.raniagus.example.config;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.nio.file.Path;
import javax.inject.Named;

public class ProductionConfiguration extends Configuration {
  @Provides
  @Named("DB")
  protected Runnable db() {
    return () -> WithSimplePersistenceUnit.configure(properties -> properties
        .set("hibernate.connection.url", ConfigurationUtil.getRequiredEnv("DB_URL"))
        .set("hibernate.connection.username", ConfigurationUtil.getRequiredEnv("DB_USERNAME"))
        .set("hibernate.connection.password", ConfigurationUtil.getRequiredEnv("DB_PASSWORD"))
        .set("hibernate.hbm2ddl.auto", "validate"));
  }

  @Provides
  @Singleton
  protected TemplateEngine templateEngine() {
    return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
  }

  @Provides
  @Singleton
  @Named("PORT")
  protected Integer port() {
    return Integer.parseInt(ConfigurationUtil.getRequiredEnv("PORT"));
  }
}
