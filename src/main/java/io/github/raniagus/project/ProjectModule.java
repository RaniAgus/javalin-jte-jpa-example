package io.github.raniagus.project;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.github.raniagus.project.access.UserAccessManager;
import io.javalin.security.AccessManager;

public class ProjectModule extends AbstractModule {
  private static final Injector injector = Guice.createInjector(new ProjectModule());

  private ProjectModule() {
  }

  public static <T> T getInstance(Class<T> clazz) {
    return injector.getInstance(clazz);
  }

  @Override
  protected void configure() {
    bind(AccessManager.class).to(UserAccessManager.class);
  }
}
