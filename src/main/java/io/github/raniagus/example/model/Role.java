package io.github.raniagus.example.model;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
  ANYONE,
  ADMIN,
  USER,
}
