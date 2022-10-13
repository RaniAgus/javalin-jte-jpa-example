package io.github.raniagus.project.model;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
  ANYONE,
  ADMIN,
  USER
}
