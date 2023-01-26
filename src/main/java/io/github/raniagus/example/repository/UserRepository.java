package io.github.raniagus.example.repository;

import static java.util.Map.entry;

import io.github.raniagus.example.model.User;
import java.util.Optional;
import javax.inject.Singleton;

@Singleton
public class UserRepository extends Repository<User> {
  public Class<User> getEntityClass() {
    return User.class;
  }

  public Optional<User> getByEmail(String username) {
    return getBy(entry("email", username));
  }
}
