package io.github.raniagus.project.view;

import io.github.raniagus.core.view.ViewModel;

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
