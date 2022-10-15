package io.github.raniagus.project.access;

import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;
import io.github.raniagus.project.repository.UserRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class UserAccessManager implements AccessManager {
  private UserRepository userRepository;

  public UserAccessManager(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context context,
                     @NotNull Set<? extends RouteRole> set) throws Exception {
    var user = userRepository.getById(context.sessionAttribute("user"));
    if (set.contains(Role.ANYONE) || user.map(User::getRole)
                                         .map(set::contains)
                                         .orElse(false)) {
      handler.handle(context);
    } else if (user.isPresent()) {
      throw new NotFoundResponse();
    } else {
      context.redirect("/login?redirect=" + context.path());
    }
  }
}
