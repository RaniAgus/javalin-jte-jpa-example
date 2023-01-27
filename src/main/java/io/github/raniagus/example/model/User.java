package io.github.raniagus.example.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Entity
@Table(name = "users")
public class User extends PersistableEntity {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;

  protected User() {
  }

  public User(String firstName, String lastName, String email, String password, Role role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = sha256Hex(password);
    this.role = role;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

  @Override
  public String toString() {
    return "User{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", role=" + role +
        '}';
  }
}
