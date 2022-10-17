package io.github.raniagus.project.repository;

import io.github.raniagus.core.repository.Repository;
import io.github.raniagus.project.model.User;
import java.util.Optional;
import javax.inject.Singleton;

@Singleton
public class UserRepository implements Repository<User> {
  public Class<User> getEntityClass() {
    return User.class;
  }

  public Optional<User> getByUsername(String username) {
    return getBy("username", username);
  }
}
