package io.github.raniagus.example.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.raniagus.example.util.WithConfiguration;

public enum InjectorHolder implements WithConfiguration {
  INSTANCE;

  private final Injector injector = Guice.createInjector(
      isProduction() ? new ProductionConfiguration() : new DevelopmentConfiguration());

  public static Injector getInjector() {
    return INSTANCE.injector;
  }
}
