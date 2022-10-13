package io.github.raniagus.project;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;

public class Bootstrap extends Entrypoint implements WithSimplePersistenceUnit {
  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    withTransaction(userRepository::deleteAll);
    withTransaction(() -> {
      userRepository.save(new User("admin", "admin", Role.ADMIN));
      userRepository.save(new User("user", "user", Role.USER));
    });
  }
}
