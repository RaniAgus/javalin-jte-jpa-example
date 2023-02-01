package io.github.raniagus.example.view;

public class LoginViewModel implements ViewModel {
  private final String email;
  private final String redirect;
  private final String error;

  public LoginViewModel(String email, String redirect, String error) {
    this.email = email;
    this.redirect = redirect;
    this.error = error;
  }

  public String getEmail() {
    return email;
  }

  public String getRedirect() {
    return redirect;
  }

  public String getError() {
    return error;
  }

  @Override
  public String getTemplateName() {
    return "login.jte";
  }
}
