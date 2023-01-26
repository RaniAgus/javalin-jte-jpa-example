package io.github.raniagus.example.view;

public class HomeViewModel implements ViewModel {
  private String firstName;
  private boolean isAdmin;

  public HomeViewModel(String firstName, boolean isAdmin) {
    this.firstName = firstName;
    this.isAdmin = isAdmin;
  }

  public String getFirstName() {
    return firstName;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  @Override
  public String getTemplateName() {
    return "home.jte";
  }
}
