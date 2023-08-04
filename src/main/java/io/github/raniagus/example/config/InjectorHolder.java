package io.github.raniagus.example.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Esta clase es un service locator: nos permite inyectar dependencias en
 * otras clases a través de una única instancia de {@link Injector}.
 * Nos será útil en caso de necesitar inyectar alguna dependencia en alguna
 * clase instanciable por Hibernate, o en el main de la aplicación.
 */
public enum InjectorHolder {
  INSTANCE;

  private final Injector injector = Guice.createInjector(
      ConfigurationUtil.isProduction() ? new ProductionConfiguration() : new DevelopmentConfiguration());

  public static Injector getInjector() {
    return INSTANCE.injector;
  }
}
