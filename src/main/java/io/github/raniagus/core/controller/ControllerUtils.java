package io.github.raniagus.core.controller;

import io.github.raniagus.core.view.ViewModel;
import io.javalin.http.Context;
import io.javalin.validation.Validator;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public interface ControllerUtils {
  static void render(Context ctx, ViewModel viewModel) {
    ctx.render(viewModel.getTemplateName(), Map.of("vm", viewModel));
  }

  static String decode(String s) {
    return s == null ? null : URLDecoder.decode(s, StandardCharsets.UTF_8);
  }

  static String encode(String... strings) {
    return URLEncoder.encode(String.join("", strings), StandardCharsets.UTF_8);
  }

  static String encode(Map<String, String> params) {
    return params.entrySet().stream()
        .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
        .collect(Collectors.joining("&"));
  }

  static <T> Validator<T> validatorOf(String fieldName, T value) {
    return new Validator<>(fieldName, value, null);
  }
}
