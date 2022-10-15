package io.github.raniagus.project.controller;

import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;
import io.github.raniagus.project.repository.UserRepository;
import io.github.raniagus.project.view.HomeViewModel;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import static io.github.raniagus.project.controller.ControllerUtils.render;

public class HomeController implements Controller {
  private UserRepository userRepository;

  public HomeController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void index(Context ctx) {
    User user = getUser(ctx);
    render(ctx, new HomeViewModel(user.getUsername(), user.getRole() == Role.ADMIN));
  }

  private User getUser(Context ctx) {
    return userRepository.getById(ctx.sessionAttribute("user"))
        .orElseThrow(NotFoundResponse::new);
  }
}
