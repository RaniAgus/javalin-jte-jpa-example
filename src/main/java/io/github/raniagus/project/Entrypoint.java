package io.github.raniagus.project;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import io.github.raniagus.project.controller.HomeController;
import io.github.raniagus.project.controller.SecurityController;
import io.github.raniagus.project.repository.UserRepository;

import java.nio.file.Path;
import java.util.Arrays;

public abstract class Entrypoint {
  public static final Boolean isProd = notNull("DB_URL", "DB_USERNAME", "DB_PASSWORD");
  public static final TemplateEngine templateEngine;

  // Repository layer
  public static final UserRepository userRepository = new UserRepository();

  // Controller layer
  public static final SecurityController securityController = new SecurityController(userRepository);
  public static final HomeController homeController = new HomeController(userRepository);

  static {
    if (isProd) {
      WithSimplePersistenceUnit.configure(properties -> properties
          .set("hibernate.connection.url", System.getenv("DB_URL"))
          .set("hibernate.connection.username", System.getenv("DB_USERNAME"))
          .set("hibernate.connection.password", System.getenv("DB_PASSWORD"))
      );
      templateEngine = TemplateEngine.createPrecompiled(
          Path.of("jte-classes"), ContentType.Html);
    } else {
      WithSimplePersistenceUnit.dispose();
      templateEngine = TemplateEngine.create(
          new DirectoryCodeResolver(Path.of("src/main/jte")), ContentType.Html);
    }
  }

  static boolean notNull(String... args) {
    return Arrays.stream(args).allMatch(arg -> System.getenv(arg) != null);
  }
}
