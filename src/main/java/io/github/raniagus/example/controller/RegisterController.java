package io.github.raniagus.example.controller;

import static io.github.raniagus.example.enumeration.ControllerConstants.ERROR;
import static io.github.raniagus.example.enumeration.UserConstants.CONFIRM_PASSWORD;
import static io.github.raniagus.example.enumeration.UserConstants.EMAIL;
import static io.github.raniagus.example.enumeration.UserConstants.FIRST_NAME;
import static io.github.raniagus.example.enumeration.UserConstants.LAST_NAME;
import static io.github.raniagus.example.enumeration.UserConstants.PASSWORD;
import static io.javalin.validation.JavalinValidation.collectErrors;

import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.RegisterViewModel;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RegisterController extends Controller {
  private final UserRepository userRepository;

  @Inject
  public RegisterController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void renderRegister(Context ctx) {
    render(ctx, new RegisterViewModel(
        ctx.queryParam(FIRST_NAME.getValue()),
        ctx.queryParam(LAST_NAME.getValue()),
        ctx.queryParam(EMAIL.getValue()),
        split(ctx.queryParamAsClass(ERROR.getValue(), String.class).getOrDefault(""), ",")
    ));
  }

  public void register(Context ctx) {
    var firstName = ctx.formParamAsClass(FIRST_NAME.getValue(), String.class);
    var lastName = ctx.formParamAsClass(LAST_NAME.getValue(), String.class);
    var email = ctx.formParamAsClass(EMAIL.getValue(), String.class)
        .check(e -> userRepository.findByEmail(e).isEmpty(), "Email already exists");
    var password = ctx.formParamAsClass(PASSWORD.getValue(), String.class)
        .check(p -> p.equals(ctx.formParam(CONFIRM_PASSWORD.getValue())), "Passwords do not match");

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
      ctx.redirect("/register?success=true");
    } catch (ValidationException e) {
      ctx.redirect("/register?" + encode(Map.of(
          FIRST_NAME.getValue(), ctx.formParamAsClass(FIRST_NAME.getValue(), String.class).getOrDefault(""),
          LAST_NAME.getValue(), ctx.formParamAsClass(LAST_NAME.getValue(), String.class).getOrDefault(""),
          EMAIL.getValue(), ctx.formParamAsClass(EMAIL.getValue(), String.class).getOrDefault(""),
          ERROR.getValue(), collectErrors(firstName, lastName, email, password).values().stream()
              .flatMap(List::stream)
              .map(ValidationError::getMessage)
              .collect(Collectors.joining(","))
      )));
    }
  }
}
