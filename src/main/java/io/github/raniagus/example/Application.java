package io.github.raniagus.example;

import com.google.inject.Inject;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.config.InjectorHolder;
import io.github.raniagus.example.controller.HomeController;
import io.github.raniagus.example.controller.LoginController;
import io.github.raniagus.example.controller.RegisterController;
import io.javalin.Javalin;
import javax.inject.Named;

public class Application implements Runnable {
  public static void main(String[] args) {
    InjectorHolder.getInjector().getInstance(HomeController.class);
    InjectorHolder.getInjector().getInstance(LoginController.class);
    InjectorHolder.getInjector().getInstance(RegisterController.class);

    InjectorHolder.getInjector().getInstance(Application.class).run();
  }

  private Javalin app;
  private Integer port;

  @Override
  public void run() {
    app.after(ctx -> WithSimplePersistenceUnit.dispose());
    app.start(port);
  }

  @Inject
  public void setApp(Javalin app) {
    this.app = app;
  }

  @Inject
  public void setPort(@Named("PORT") Integer port) {
    this.port = port;
  }
}
