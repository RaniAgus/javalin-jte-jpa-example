package io.github.raniagus.example.view;

import java.util.List;

public class RegisterViewModel implements ViewModel {
  private String firstName;
  private String lastName;
  private String email;
  private List<String> errors;

  public RegisterViewModel(String firstName, String lastName, String email, List<String> error) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.errors = error;
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

  public List<String> getErrors() {
    return errors;
  }

  @Override
  public String getTemplateName() {
    return "register.jte";
  }
}
