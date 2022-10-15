package io.github.raniagus.project.controller;

import io.github.raniagus.project.repository.UserRepository;
import io.github.raniagus.project.view.LoginViewModel;
import io.github.raniagus.project.view.NotFoundViewModel;
import io.github.raniagus.project.view.RegisterViewModel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import java.util.Map;

import static io.github.raniagus.project.controller.ControllerUtils.decode;
import static io.github.raniagus.project.controller.ControllerUtils.encode;
import static io.github.raniagus.project.controller.ControllerUtils.render;
import static io.javalin.validation.JavalinValidation.collectErrors;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class UserController implements Controller {
  private UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void renderLogin(Context ctx) {
    var redirect = ctx.queryParam("redirect");
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
      ctx.status(HttpStatus.UNAUTHORIZED);
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
}
