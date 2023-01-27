package io.github.raniagus.example.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CSVUser {
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("email")
  private String email;
  @JsonProperty("password")
  private String password;
  @JsonProperty("is_admin")
  private boolean isAdmin;

  public User toUser() {
    return new User(
        firstName,
        lastName,
        email,
        password,
        isAdmin ? Role.ADMIN : Role.USER
    );
  }
}
