package io.github.raniagus.example.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public enum InjectorHolder {
  INSTANCE;

  private final Injector injector = Guice.createInjector(Configuration.create());

  public static <T> T getInstance(Class<T> clazz) {
    return INSTANCE.injector.getInstance(clazz);
  }
}
