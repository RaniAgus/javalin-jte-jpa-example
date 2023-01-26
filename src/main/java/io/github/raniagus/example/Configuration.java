package io.github.raniagus.example;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.github.raniagus.example.access.UserAccessManager;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class is used to configure the database connection,
 * the template engine, and the access manager.
 */
public class Configuration extends AbstractModule {
  private static final Injector INJECTOR = Guice.createInjector(new Configuration());

  private Configuration() {
  }

  public static <T> T getInstanceOf(Class<T> clazz) {
    return INJECTOR.getInstance(clazz);
  }

  @Override
  protected void configure() {
    connectToSimplePersistenceUnit();
    bind(AccessManager.class).to(UserAccessManager.class);
    bind(FileRenderer.class).toInstance(fileRenderer());
  }

  private void connectToSimplePersistenceUnit() {
    if (isProd()) {
      WithSimplePersistenceUnit.configure(properties -> properties
          .set("hibernate.connection.url", System.getenv("DB_URL"))
          .set("hibernate.connection.username", System.getenv("DB_USERNAME"))
          .set("hibernate.connection.password", System.getenv("DB_PASSWORD")));
    } else {
      WithSimplePersistenceUnit.dispose();
    }
  }

  private FileRenderer fileRenderer() {
    return new JavalinJte(templateEngine(), (ctx) -> !isProd());
  }

  private TemplateEngine templateEngine() {
    return isProd() ?
        TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html) :
        TemplateEngine.create(new DirectoryCodeResolver(Path.of("src","main", "jte")), ContentType.Html);
  }

  private boolean isProd() {
    return Stream.of("DB_URL", "DB_USERNAME", "DB_PASSWORD")
        .map(System::getenv).allMatch(Objects::nonNull);
  }
}
