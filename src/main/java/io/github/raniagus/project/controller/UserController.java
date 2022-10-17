package io.github.raniagus.project.controller;

import io.github.raniagus.core.controller.Controller;
import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;
import io.github.raniagus.project.repository.UserRepository;
import io.github.raniagus.project.view.LoginViewModel;
import io.github.raniagus.project.view.NotFoundViewModel;
import io.github.raniagus.project.view.RegisterViewModel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import static io.github.raniagus.core.controller.ControllerUtils.decode;
import static io.github.raniagus.core.controller.ControllerUtils.encode;
import static io.github.raniagus.core.controller.ControllerUtils.render;
import static io.javalin.validation.JavalinValidation.collectErrors;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Singleton
public class UserController implements Controller {
  private UserRepository userRepository;

  @Inject
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void renderLogin(Context ctx) {
    var redirect = ctx.queryParamAsClass("redirect", String.class).getOrDefault("/");
    var error = ctx.queryParam("error");

    render(ctx, new LoginViewModel(redirect, decode(error)));
  }

  public void login(Context ctx) {
    var username = ctx.formParamAsClass("username", String.class);
    var password = ctx.formParamAsClass("password", String.class);
    var redirect = ctx.formParamAsClass("redirect", String.class).getOrDefault("/");

    try {
      var user = userRepository.getByUsername(username.get())
          .filter(u -> u.getPassword().equals(sha256Hex(password.get())))
          .orElseThrow(() -> new NotFoundResponse("Invalid username or password"));

      ctx.sessionAttribute("user", user.getId());
      ctx.redirect(redirect);
    } catch (ValidationException e) {
      ctx.status(HttpStatus.BAD_REQUEST);
      ctx.json(collectErrors(username, password));
    } catch (NotFoundResponse e) {
      ctx.redirect("/login?" + encode(Map.of("redirect", redirect, "error", e.getMessage())));
    }
  }

  public void logout(Context ctx) {
    ctx.consumeSessionAttribute("user");
    ctx.redirect("/login");
  }

  public void notFound(NotFoundResponse e, Context ctx) {
    ctx.status(HttpStatus.NOT_FOUND);
    render(ctx, new NotFoundViewModel());
  }

  public void renderRegister(Context ctx) {
    render(ctx, new RegisterViewModel(ctx.queryParam("error")));
  }

  public void register(Context ctx) {
    var username = ctx.formParamAsClass("username", String.class)
        .check(u -> userRepository.getByUsername(u).isEmpty(), "Username already exists");
    var password = ctx.formParamAsClass("password", String.class)
        .check(p -> p.equals(ctx.formParam("confirm-password")), "Passwords do not match");

    try {
      withTransaction(() -> {
        var user = new User(username.get(), password.get(), Role.USER);
        userRepository.save(user);
      });
      ctx.redirect("/register?success=true");
    } catch (ValidationException e) {
      ctx.status(HttpStatus.BAD_REQUEST);
      ctx.json(collectErrors(username, password));
    } catch (IllegalArgumentException e) {
      ctx.redirect("/register?" + encode(Map.of("error", e.getMessage())));
    }
  }
}
