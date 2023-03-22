package io.github.raniagus.example.enumeration;

public enum UserConstants {
  CONFIRM_PASSWORD("confirm-password"),
  EMAIL("email"),
  FIRST_NAME("first-name"),
  LAST_NAME("last-name"),
  PASSWORD("password"),
  USER("user"),
  ;

  private final String value;

  UserConstants(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
