package io.github.raniagus.example.enumeration;

public enum ControllerConstants {
  ERROR("error"),
  REDIRECT("redirect"),
  ;

  private final String value;

  ControllerConstants(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
