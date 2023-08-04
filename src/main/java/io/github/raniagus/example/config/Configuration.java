package io.github.raniagus.example.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import gg.jte.TemplateEngine;
import io.github.raniagus.example.access.UserAccessManager;
import io.github.raniagus.example.util.WithConfiguration;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import io.javalin.validation.JavalinValidation;
import java.time.LocalDate;

public abstract class Configuration extends AbstractModule implements WithConfiguration {

  @Provides @Singleton
  protected Javalin app(TemplateEngine templateEngine, @Named("DB") Runnable db, AccessManager accessManager) {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(new JavalinJte(templateEngine, ctx -> !isProduction()), ".jte");
    db.run();
    return Javalin.create(config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(accessManager);
    });
  }

  @Override
  protected void configure() {
    bind(AccessManager.class).to(UserAccessManager.class);
  }

}
