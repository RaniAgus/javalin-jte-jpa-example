package io.github.raniagus.example.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import org.apache.commons.lang3.RandomStringUtils;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Entity
@Table(name = "users")
public class User extends PersistableEntity {
  private String firstName;
  private String lastName;
  private String email;
  private String passwordSalt;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;

  protected User() {
  }

  public User(String firstName, String lastName, String email, String password, Role role) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.passwordSalt = RandomStringUtils.random(16);
    this.password = getHashedPassword(password, this.passwordSalt);
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

  public boolean hasPassword(String password) {
    return this.password.equals(getHashedPassword(password, this.passwordSalt));
  }

  public Role getRole() {
    return role;
  }

  private static String getHashedPassword(String password, String salt) {
    return sha256Hex(salt + sha256Hex(password));
  }

  @Override
  public String toString() {
    return "User{" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", passwordSalt='" + passwordSalt + '\'' +
        ", password='" + password + '\'' +
        ", role=" + role +
        '}';
  }
}
