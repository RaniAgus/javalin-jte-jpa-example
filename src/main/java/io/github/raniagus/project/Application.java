package io.github.raniagus.project;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.core.Entrypoint;
import io.github.raniagus.project.controller.HomeController;
import io.github.raniagus.project.controller.UserController;
import io.github.raniagus.project.model.Role;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.security.AccessManager;
import io.javalin.validation.JavalinValidation;
import java.time.LocalDate;
import java.util.Optional;

import static io.javalin.apibuilder.ApiBuilder.after;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Application extends Entrypoint {
  private static final UserController userController = ProjectModule.getInstance(UserController.class);
  private static final HomeController homeController = ProjectModule.getInstance(HomeController.class);

  public static void main(String[] args) {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(new JavalinJte(templateEngine, (ctx) -> !isProd), ".jte");
    Javalin.create(config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(ProjectModule.getInstance(AccessManager.class));
    }).routes(() -> {
      get("/", homeController::index, Role.USER, Role.ADMIN);
      get("/login", userController::renderLogin, Role.ANYONE);
      post("/login", userController::login, Role.ANYONE);
      get("/register", userController::renderRegister, Role.ADMIN);
      post("/register", userController::register, Role.ADMIN);
      get("/logout", userController::logout, Role.USER, Role.ADMIN);
      after(ctx -> WithSimplePersistenceUnit.dispose());
    }).exception(NotFoundResponse.class, userController::notFound)
      .start(getPort());
  }

  static Integer getPort() {
    return Optional.ofNullable(System.getenv("PORT"))
        .map(Integer::parseInt)
        .orElse(8080);
  }
}
