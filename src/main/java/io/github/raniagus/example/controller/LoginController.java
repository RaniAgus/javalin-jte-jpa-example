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
import static io.github.raniagus.example.enumeration.ControllerConstants.ERROR;
import static io.github.raniagus.example.enumeration.ControllerConstants.REDIRECT;
import static io.github.raniagus.example.enumeration.UserConstants.EMAIL;
import static io.github.raniagus.example.enumeration.UserConstants.PASSWORD;
import static io.github.raniagus.example.enumeration.UserConstants.USER;
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
    var email = ctx.queryParam(EMAIL.getValue());
    var redirect = ctx.queryParamAsClass(REDIRECT.getValue(), String.class).getOrDefault("/");
    var error = ctx.queryParam(ERROR.getValue());

    render(ctx, new LoginViewModel(email, redirect, decode(error)));
  }

  public void login(Context ctx) {
    var email = ctx.formParamAsClass(EMAIL.getValue(), String.class);
    var password = ctx.formParamAsClass(PASSWORD.getValue(), String.class);
    var redirect = ctx.formParamAsClass(REDIRECT.getValue(), String.class).getOrDefault("/");

    try {
      var user = userRepository.findByEmail(email.get())
          .filter(u -> u.hasPassword(password.get()))
          .orElseThrow(() -> new NotFoundResponse("Invalid email or password"));

      ctx.sessionAttribute(USER.getValue(), user.getId());
      ctx.redirect(redirect);
    } catch (ValidationException e) {
      ctx.status(HttpStatus.BAD_REQUEST);
      ctx.json(collectErrors(email, password));
    } catch (NotFoundResponse e) {
      ctx.redirect("/login?" + encode(Map.of(
          EMAIL.getValue(), ctx.formParamAsClass(EMAIL.getValue(), String.class).getOrDefault(""),
          REDIRECT.getValue(), redirect,
          ERROR.getValue(), e.getMessage()
      )));
    }
  }

  public void logout(Context ctx) {
    ctx.consumeSessionAttribute(USER.getValue());
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
