package io.github.raniagus.project.view;

public class RegisterViewModel implements ViewModel {
  private String error;

  public RegisterViewModel(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  @Override
  public String getTemplateName() {
    return "register.jte";
  }
}
