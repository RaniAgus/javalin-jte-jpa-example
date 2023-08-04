package io.github.raniagus.example.controller;

import static io.github.raniagus.example.enumeration.Params.*;
import static io.github.raniagus.example.enumeration.UserFields.*;
import static io.javalin.validation.JavalinValidation.collectErrors;

import io.github.raniagus.example.enumeration.Routes;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.RegisterViewModel;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegisterController implements Controller {
  private UserRepository userRepository;

  @Inject
  public RegisterController(Javalin app) {
    app.get(Routes.REGISTER, this::renderRegister, Role.ADMIN);
    app.post(Routes.REGISTER, this::register, Role.ADMIN);
  }

  public void renderRegister(Context ctx) {
    render(ctx, new RegisterViewModel(
        ctx.queryParam(FIRST_NAME),
        ctx.queryParam(LAST_NAME),
        ctx.queryParam(EMAIL),
        split(ctx.queryParamAsClass(ERROR, String.class).getOrDefault(""), ",")
    ));
  }

  public void register(Context ctx) {
    var firstName = ctx.formParamAsClass(FIRST_NAME, String.class);
    var lastName = ctx.formParamAsClass(LAST_NAME, String.class);
    var email = ctx.formParamAsClass(EMAIL, String.class)
        .check(e -> userRepository.findByEmail(e).isEmpty(), "Email already exists");
    var password = ctx.formParamAsClass(PASSWORD, String.class)
        .check(p -> p.equals(ctx.formParam(CONFIRM_PASSWORD)), "Passwords do not match");

    try {
      withTransaction(() -> {
        var user = new User(
            firstName.get(),
            lastName.get(),
            email.get(),
            password.get(),
            Role.USER);
        userRepository.save(user);
      });
      redirect(ctx, Routes.REGISTER, Map.of("success", true));
    } catch (ValidationException e) {
      redirect(ctx, Routes.REGISTER, Map.of(
          FIRST_NAME, ctx.formParamAsClass(FIRST_NAME, String.class).getOrDefault(""),
          LAST_NAME, ctx.formParamAsClass(LAST_NAME, String.class).getOrDefault(""),
          EMAIL, ctx.formParamAsClass(EMAIL, String.class).getOrDefault(""),
          ERROR, collectErrors(firstName, lastName, email, password).values().stream()
              .flatMap(List::stream)
              .map(ValidationError::getMessage)
              .collect(Collectors.joining(","))
      ));
    }
  }

  @Inject
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
}
