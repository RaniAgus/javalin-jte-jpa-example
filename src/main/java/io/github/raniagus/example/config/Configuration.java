package io.github.raniagus.example.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import gg.jte.TemplateEngine;
import io.github.raniagus.example.access.UserAccessManager;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import io.javalin.validation.JavalinValidation;
import java.time.LocalDate;

/**
 * Esta clase nos permite configurar la inyección de dependencias de Guice desde el método configure().
 */
public abstract class Configuration extends AbstractModule {
  @Provides
  @Singleton
  public static Javalin app(TemplateEngine templateEngine, @Named("DB") Runnable db, AccessManager accessManager) {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(new JavalinJte(templateEngine, ctx -> !ConfigurationUtil.isProduction()), ".jte");
    db.run();
    return Javalin.create(config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(accessManager);
    });
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
  }
}
