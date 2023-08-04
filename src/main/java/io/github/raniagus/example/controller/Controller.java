package io.github.raniagus.example.controller;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.util.WithConfiguration;
import io.github.raniagus.example.util.WithEncodingUtil;
import io.github.raniagus.example.util.WithStringUtil;
import io.github.raniagus.example.view.ViewModel;
import io.javalin.http.Context;
import java.util.Map;

public interface Controller extends WithSimplePersistenceUnit, WithEncodingUtil, WithStringUtil, WithConfiguration {
  default void render(Context ctx, ViewModel viewModel) {
    ctx.render(viewModel.getTemplateName(), Map.of("vm", viewModel));
  }
}
