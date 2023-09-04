package io.github.raniagus.example.access;

import static io.github.raniagus.example.enumeration.Params.REDIRECT;
import static io.github.raniagus.example.enumeration.UserFields.*;

import io.github.raniagus.example.controller.Controller;
import io.github.raniagus.example.enumeration.Routes;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class UserAccessManager implements AccessManager, Controller {
  private final UserRepository userRepository;

  @Inject
  public UserAccessManager(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context context,
                     @NotNull Set<? extends RouteRole> routeRoles) throws Exception {
    var userOpt = userRepository.findById(context.sessionAttribute(USER));
    if (routeRoles.contains(Role.ANYONE) || userOpt.map(User::getRole).filter(routeRoles::contains).isPresent()) {
      handler.handle(context);
    } else if (userOpt.isPresent()) {
      throw new NotFoundResponse();
    } else {
      redirect(context, Routes.LOGIN, Map.of(REDIRECT, context.path()));
    }
  }
}
