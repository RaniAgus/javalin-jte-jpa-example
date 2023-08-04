package io.github.raniagus.example.controller;

import static io.github.raniagus.example.enumeration.UserConstants.USER;

import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.HomeViewModel;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeController extends Controller {
  private UserRepository userRepository;

  @Inject
  public HomeController(Javalin app) {
    app.get("/", this::index, Role.USER, Role.ADMIN);
  }

  public void index(Context ctx) {
    User user = userRepository.findById(ctx.sessionAttribute(USER.getValue()))
        .orElseThrow(NotFoundResponse::new);

    render(ctx, new HomeViewModel(user.getFirstName(),user.getRole() == Role.ADMIN));
  }

  @Inject
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
