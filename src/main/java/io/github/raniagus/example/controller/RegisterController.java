package io.github.raniagus.example.controller;

import static io.javalin.validation.JavalinValidation.collectErrors;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import io.github.raniagus.example.repository.UserRepository;
import io.github.raniagus.example.view.RegisterViewModel;
import io.javalin.http.Context;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import java.util.Map;
import java.util.Objects;
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
    var errors = Splitter.on(",").omitEmptyStrings()
        .split(Objects.requireNonNullElse(ctx.queryParam("errors"), ""));

    render(ctx, new RegisterViewModel(
        ctx.queryParam("first-name"),
        ctx.queryParam("last-name"),
        ctx.queryParam("email"),
        ImmutableList.copyOf(errors)
    ));
  }

  public void register(Context ctx) {
    var firstName = ctx.formParamAsClass("first-name", String.class);
    var lastName = ctx.formParamAsClass("last-name", String.class);
    var email = ctx.formParamAsClass("email", String.class)
        .check(e -> userRepository.findByEmail(e).isEmpty(), "Email already exists");
    var password = ctx.formParamAsClass("password", String.class)
        .check(p -> p.equals(ctx.formParam("confirm-password")), "Passwords do not match");

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
      var errors = collectErrors(firstName, lastName, email, password).entrySet().stream()
          .flatMap(entry -> entry.getValue().stream())
          .map(ValidationError::getMessage)
          .collect(Collectors.joining(","));

      ctx.redirect("/register?" + encode(Map.of(
          "first-name", ctx.formParamAsClass("first-name", String.class).getOrDefault(""),
          "last-name", ctx.formParamAsClass("last-name", String.class).getOrDefault(""),
          "email", ctx.formParamAsClass("email", String.class).getOrDefault(""),
          "errors", errors
      )));
    }
  }
}
