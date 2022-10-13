package io.github.raniagus.project;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.project.model.Role;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.JavalinValidation;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

import static io.javalin.apibuilder.ApiBuilder.after;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Application extends Entrypoint {
  public static void main(String[] args) {
    Javalin.create(getJavalinConfig()).start(getPort()).routes(() -> {
      get("/", homeController::index, Role.USER, Role.ADMIN);
      get("/login", securityController::renderLogin, Role.ANYONE);
      post("/login", securityController::login, Role.ANYONE);
      get("/logout", securityController::logout, Role.USER, Role.ADMIN);
      after(ctx -> WithSimplePersistenceUnit.dispose());
    }).exception(NotFoundResponse.class, securityController::notFound);
  }

  static Consumer<JavalinConfig> getJavalinConfig() {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(new JavalinJte(templateEngine, (ctx) -> !isProd), ".jte");
    return config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(securityController);
    };
  }

  static Integer getPort() {
    return Optional.ofNullable(System.getenv("PORT"))
        .map(Integer::parseInt)
        .orElse(8080);
  }
}
