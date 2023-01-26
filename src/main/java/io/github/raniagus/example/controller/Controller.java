package io.github.raniagus.example.controller;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.view.ViewModel;
import io.javalin.http.Context;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Controller implements WithSimplePersistenceUnit {
  protected void render(Context ctx, ViewModel viewModel) {
    ctx.render(viewModel.getTemplateName(), Map.of("vm", viewModel));
  }

  protected String decode(String s) {
    return s == null ? null : URLDecoder.decode(s, StandardCharsets.UTF_8);
  }

  protected String encode(String... strings) {
    return URLEncoder.encode(String.join("", strings), StandardCharsets.UTF_8);
  }

  protected String encode(Map<String, String> params) {
    return params.entrySet().stream()
        .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
        .collect(Collectors.joining("&"));
  }
}
