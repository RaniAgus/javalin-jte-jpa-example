package io.github.raniagus.example;

import com.google.inject.Inject;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.config.InjectorHolder;
import io.github.raniagus.example.csv.CSVParser;
import io.github.raniagus.example.csv.CSVUser;
import io.github.raniagus.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase nos permite insertar datos de prueba en la base de datos.
 */
public class Bootstrap implements Runnable, WithSimplePersistenceUnit {
  private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

  public static void main(String[] args) {
    InjectorHolder.getInjector().getInstance(Bootstrap.class).run();
  }

  private final UserRepository userRepository;

  @Inject
  public Bootstrap(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void run() {
    try (var reader = new CSVParser("data/users.csv", ",")) {
      var users = reader.parse(CSVUser.class)
          .map(CSVUser::toEntity)
          .toList();

      withTransaction(() -> {
        userRepository.removeAll();
        userRepository.saveAll(users);
      });

      users.forEach(user -> log.info("Usuario insertado: {}", user));
    } catch (Exception e) {
      log.error("Error al insertar datos de prueba", e);
    }
  }
}
