package io.github.raniagus.project.repository;

import io.github.raniagus.project.model.User;

import java.util.Optional;

public class UserRepository implements Repository<User> {
  public Class<User> getEntityClass() {
    return User.class;
  }

  public Optional<User> getByUsername(String username) {
    return getBy("username", username);
  }
}
