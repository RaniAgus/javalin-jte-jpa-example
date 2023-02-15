package io.github.raniagus.example;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import com.google.inject.Inject;
import io.github.raniagus.example.config.InjectorHolder;
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
import javax.inject.Named;

public class Application implements Runnable {
  public static void main(String[] args) {
    InjectorHolder.getInjector().getInstance(Application.class).run();
  }

  private final AccessManager accessManager;
  private final FileRenderer fileRenderer;
  private final HomeController homeController;
  private final LoginController loginController;
  private final RegisterController registerController;
  private final Integer port;

  @Inject
  public Application(AccessManager accessManager,
                     FileRenderer fileRenderer,
                     HomeController homeController,
                     LoginController loginController,
                     RegisterController registerController,
                     @Named("PORT") Integer port) {
    this.accessManager = accessManager;
    this.fileRenderer = fileRenderer;
    this.homeController = homeController;
    this.loginController = loginController;
    this.registerController = registerController;
    this.port = port;
  }

  @Override
  public void run() {
    JavalinValidation.register(LocalDate.class, LocalDate::parse);
    JavalinRenderer.register(fileRenderer, ".jte");

    var app = Javalin.create(config -> {
      config.staticFiles.add("public", Location.EXTERNAL);
      config.accessManager(accessManager);
    });

    app.get("/", homeController::index, Role.USER, Role.ADMIN);
    app.get("/login", loginController::renderLogin, Role.ANYONE);
    app.post("/login", loginController::login, Role.ANYONE);
    app.get("/register", registerController::renderRegister, Role.ADMIN);
    app.post("/register", registerController::register, Role.ADMIN);
    app.post("/logout", loginController::logout, Role.USER, Role.ADMIN);
    app.exception(NotFoundResponse.class, loginController::notFound);
    app.after(ctx -> WithSimplePersistenceUnit.dispose());

    app.start(port);
  }
}
