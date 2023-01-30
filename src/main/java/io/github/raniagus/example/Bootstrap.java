package io.github.raniagus.example;

import static io.github.raniagus.example.Configuration.getInstanceOf;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.csv.CSVParser;
import io.github.raniagus.example.csv.CSVUser;
import io.github.raniagus.example.repository.UserRepository;
import java.util.stream.Collectors;

/**
 * Esta clase nos permite insertar datos de prueba en la base de datos.
 */
public class Bootstrap implements WithSimplePersistenceUnit {
  public static final UserRepository userRepository = getInstanceOf(UserRepository.class);

  public static void main(String[] args) {
    new Bootstrap().run();
  }

  public void run() {
    var reader = new CSVParser("db/users.csv", ",");
    var users = reader.parse(CSVUser.class)
        .map(CSVUser::toUser)
        .collect(Collectors.toList());

    withTransaction(() -> {
      userRepository.deleteAll();
      userRepository.saveAll(users);
    });

    users.forEach(System.out::println);
  }
}
