package io.github.raniagus.example.controller;

import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.HomeViewModel;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeController extends Controller {
  private UserRepository userRepository;

  @Inject
  public HomeController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void index(Context ctx) {
    User user = getUser(ctx);
    render(ctx, new HomeViewModel(user.getFirstName(),user.getRole() == Role.ADMIN));
  }

  private User getUser(Context ctx) {
    return userRepository.getById(ctx.sessionAttribute("user"))
        .orElseThrow(NotFoundResponse::new);
  }
}
