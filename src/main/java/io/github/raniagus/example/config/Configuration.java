package io.github.raniagus.example.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import gg.jte.TemplateEngine;
import io.github.raniagus.example.access.UserAccessManager;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import java.util.Optional;

/**
 * Esta clase nos permite configurar la inyección de dependencias de Guice desde el método configure().
 */
public abstract class Configuration extends AbstractModule {
  /**
   * Este método nos permite crear una instancia de la configuración que corresponda, dependiendo de si estamos
   * desarrollando la aplicación en local o desplegándola en producción.
   */
  public static Configuration create() {
    return isProduction() ? new ProductionConfiguration() : new DevelopmentConfiguration();
  }

  public static boolean isProduction() {
    return getOptionalEnv("PRODUCTION").map(Boolean::parseBoolean).orElse(false);
  }

  protected static Optional<String> getOptionalEnv(String key) {
    return Optional.ofNullable(System.getenv(key));
  }

  protected static String getRequiredEnv(String key) {
    return getOptionalEnv(key).orElseThrow(() -> new IllegalStateException("Missing environment variable: " + key));
  }

  /**
   * En este método debemos agregar todas las clases que queremos que sean inyectadas y no se puedan resolver utilizando
   * solamente la anotación @Inject.
   * Por ejemplo, si queremos que el puerto de la aplicación sea configurable, debemos agregarlo como un binding, pero
   * todos los repositorios y controllers que queramos inyectar no necesitan ser agregados acá.
   */
  @Override
  protected void configure() {
    bind(AccessManager.class).to(UserAccessManager.class);
    bind(FileRenderer.class).toInstance(new JavalinJte(templateEngine(), ctx -> !isProduction()));
    bind(Integer.class).annotatedWith(Names.named("PORT")).toInstance(port());
    connectToSimplePersistenceUnit();
  }

  protected abstract TemplateEngine templateEngine();

  protected abstract void connectToSimplePersistenceUnit();

  protected abstract Integer port();
}
