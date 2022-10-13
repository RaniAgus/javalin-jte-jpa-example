package io.github.raniagus.project.view;

public class LoginViewModel implements ViewModel {
  private String redirect;
  private String error;

  public LoginViewModel(String redirect, String error) {
    this.redirect = redirect;
    this.error = error;
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
