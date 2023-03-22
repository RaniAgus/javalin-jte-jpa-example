package io.github.raniagus.example.controller;

import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.LoginViewModel;
import io.github.raniagus.example.view.NotFoundViewModel;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.raniagus.example.config.Configuration.isDev;
import static io.javalin.validation.JavalinValidation.collectErrors;

@Singleton
public class LoginController extends Controller {
  private static final Logger log = LoggerFactory.getLogger(LoginController.class);

  private final UserRepository userRepository;

  @Inject
  public LoginController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void renderLogin(Context ctx) {
    var email = ctx.queryParam("email");
    var redirect = ctx.queryParamAsClass("redirect", String.class).getOrDefault("/");
    var error = ctx.queryParam("error");

    render(ctx, new LoginViewModel(email, redirect, decode(error)));
  }

  public void login(Context ctx) {
    var email = ctx.formParamAsClass("email", String.class);
    var password = ctx.formParamAsClass("password", String.class);
    var redirect = ctx.formParamAsClass("redirect", String.class).getOrDefault("/");

    try {
      var user = userRepository.findByEmail(email.get())
          .filter(u -> u.hasPassword(password.get()))
          .orElseThrow(() -> new NotFoundResponse("Invalid email or password"));

      ctx.sessionAttribute("user", user.getId());
      ctx.redirect(redirect);
    } catch (ValidationException e) {
      ctx.status(HttpStatus.BAD_REQUEST);
      ctx.json(collectErrors(email, password));
    } catch (NotFoundResponse e) {
      ctx.redirect("/login?" + encode(Map.of(
          "email", ctx.formParamAsClass("email", String.class).getOrDefault(""),
          "redirect", redirect,
          "error", e.getMessage()
      )));
    }
  }

  public void logout(Context ctx) {
    ctx.consumeSessionAttribute("user");
    ctx.redirect("/login");
  }

  public void notFound(NotFoundResponse e, Context ctx) {
    ctx.status(HttpStatus.NOT_FOUND);
    if (isDev()) {
      log.warn("Not found {}", ctx.req().getRequestURI(), e);
    }
    render(ctx, new NotFoundViewModel());
  }
}
