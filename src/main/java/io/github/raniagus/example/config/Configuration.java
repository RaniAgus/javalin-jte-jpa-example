package io.github.raniagus.example.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import gg.jte.TemplateEngine;
import io.github.raniagus.example.access.UserAccessManager;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import java.util.List;
import java.util.Objects;

/**
 * Esta clase nos permite configurar la inyección de dependencias de Guice desde el método configure().
 */
public abstract class Configuration extends AbstractModule {
  /**
   * Este método nos permite crear una instancia de la configuración que corresponda, dependiendo de si estamos
   * desarrollando la aplicación en local o desplegándola en producción.
   */
  public static Configuration create() {
    return isDev() ? new DevConfiguration() : new ProdConfiguration();
  }

  protected static boolean isDev() {
    var variables = List.of("DB_URL", "DB_USERNAME", "DB_PASSWORD", "PORT");
    var noneVariableExist = variables.stream().map(System::getenv).noneMatch(Objects::nonNull);
    var allVariablesExist = variables.stream().map(System::getenv).allMatch(Objects::nonNull);

    // Si existen algunas variables de entorno, pero no todas, hay que revisar la configuración.
    if (!noneVariableExist && !allVariablesExist) {
      throw new IllegalStateException("Missing environment variables: " + String.join(", ", variables));
    }

    return noneVariableExist;
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
    bind(FileRenderer.class).toInstance(new JavalinJte(templateEngine(), (ctx) -> isDev()));
    bind(Integer.class).annotatedWith(Names.named("PORT")).toInstance(port());
    connectToSimplePersistenceUnit();
  }

  protected abstract TemplateEngine templateEngine();

  protected abstract void connectToSimplePersistenceUnit();

  protected abstract Integer port();
}
