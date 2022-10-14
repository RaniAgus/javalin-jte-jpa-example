package io.github.raniagus.project.view;

public class HomeViewModel implements ViewModel {
  public String username;
  public boolean isAdmin;

  public HomeViewModel(String username, boolean isAdmin) {
    this.username = username;
    this.isAdmin = isAdmin;
  }

  public String getUsername() {
    return username;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  @Override
  public String getTemplateName() {
    return "home.jte";
  }
}
