package io.github.raniagus.project.model;

import io.github.raniagus.core.model.PersistableEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Entity
@Table(name = "users")
public class User extends PersistableEntity {
  private String username;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;

  protected User() {
  }

  public User(String username, String password, Role role) {
    this.username = username;
    this.password = sha256Hex(password);
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }
}
