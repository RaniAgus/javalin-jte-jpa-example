package io.github.raniagus.project.controller;

import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;
import io.github.raniagus.project.repository.UserRepository;
import io.github.raniagus.project.view.LoginViewModel;
import io.github.raniagus.project.view.NotFoundViewModel;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import io.javalin.validation.ValidationException;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

import static io.github.raniagus.project.controller.ControllerUtils.decode;
import static io.github.raniagus.project.controller.ControllerUtils.encode;
import static io.github.raniagus.project.controller.ControllerUtils.render;
import static io.javalin.validation.JavalinValidation.collectErrors;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

public class SecurityController implements Controller, AccessManager {
  private UserRepository userRepository;

  public SecurityController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void manage(@NotNull Handler handler,
                     @NotNull Context context,
                     @NotNull Set<? extends RouteRole> set) throws Exception {
    var user = userRepository.getById(context.sessionAttribute("user"));
    if (set.contains(Role.ANYONE) || user.map(User::getRole).map(set::contains).orElse(false)) {
      handler.handle(context);
    } else if (user.isPresent()) {
      throw new NotFoundResponse();
    } else {
      context.redirect("/login?redirect=" + context.path());
    }
  }

  public void renderLogin(Context ctx) {
    var redirect = ctx.queryParam("redirect");
    var error = ctx.queryParam("error");

    render(ctx, new LoginViewModel(redirect, decode(error)));
  }

  public void login(Context ctx) {
    var username = ctx.formParamAsClass("username", String.class);
    var password = ctx.formParamAsClass("password", String.class);
    var redirect = ctx.queryParamAsClass("redirect", String.class).getOrDefault("/");

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
}
