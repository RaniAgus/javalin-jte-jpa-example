package io.github.raniagus.example;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.controller.HomeController;
import io.github.raniagus.example.controller.LoginController;
import io.github.raniagus.example.controller.RegisterController;
import io.github.raniagus.example.model.Role;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.security.AccessManager;
import io.javalin.validation.JavalinValidation;
import java.time.LocalDate;
import java.util.Optional;

import static io.github.raniagus.example.Configuration.getInstanceOf;

public class Application {
  private static final HomeController homeController = getInstanceOf(HomeController.class);
  private static final LoginController loginController = getInstanceOf(LoginController.class);
  private static final RegisterController registerController = getInstanceOf(RegisterController.class);

  public static void main(String[] args) {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(getInstanceOf(FileRenderer.class), ".jte");
    Javalin app = Javalin.create(config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(getInstanceOf(AccessManager.class));
    });

    app.get("/", homeController::index, Role.USER, Role.ADMIN);
    app.get("/login", loginController::renderLogin, Role.ANYONE);
    app.post("/login", loginController::login, Role.ANYONE);
    app.get("/register", registerController::renderRegister, Role.ADMIN);
    app.post("/register", registerController::register, Role.ADMIN);
    app.post("/logout", loginController::logout, Role.USER, Role.ADMIN);
    app.exception(NotFoundResponse.class, loginController::notFound);
    app.after(ctx -> WithSimplePersistenceUnit.dispose());

    app.start(getPort());
  }

  static Integer getPort() {
    return Optional.ofNullable(System.getenv("PORT"))
        .map(Integer::parseInt)
        .orElse(8080);
  }
}
