package io.github.raniagus.project.view;

public class HomeViewModel implements ViewModel {
  public String username;

  public HomeViewModel(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public String getTemplateName() {
    return "home.jte";
  }
}
