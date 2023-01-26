package io.github.raniagus.example;

import static io.github.raniagus.example.Configuration.getInstanceOf;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.model.CSVParser;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import java.util.Objects;

/**
 * This class is used to seed the database with some initial data.
 */
public class Bootstrap implements WithSimplePersistenceUnit {
  public static final UserRepository userRepository = getInstanceOf(UserRepository.class);

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    withTransaction(() -> {
      userRepository.deleteAll();
      try (var parser = new CSVParser("db/users.csv", ",")) {
          parser.parse(parts -> new User(parts[1], parts[2], parts[3], parts[4],
                  Objects.equals(parts[5], "true") ? Role.ADMIN : Role.USER))
              .forEach(userRepository::save);
      }
    });
  }
}
