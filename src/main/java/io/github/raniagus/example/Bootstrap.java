package io.github.raniagus.example;

import static io.github.raniagus.example.Configuration.getInstanceOf;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

/**
 * This class is used to seed the database with some initial data.
 */
public class Bootstrap implements WithSimplePersistenceUnit {
  public static final UserRepository userRepository = getInstanceOf(UserRepository.class);

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    try (var scanner = new Scanner(new File("db/users.csv"))) {
      withTransaction(userRepository::deleteAll);
      withTransaction(() -> {
        scanner.nextLine(); // skip header
        while (scanner.hasNext()) {
          var parts = scanner.nextLine().split(",");
          userRepository.save(new User(parts[1], parts[2], parts[3], parts[4],
              Boolean.parseBoolean(parts[5]) ? Role.ADMIN : Role.USER));
        }
      });
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
