package io.github.raniagus.example.util;

import java.util.Optional;

public interface WithConfiguration {
  default boolean isProduction() {
    return getOptionalEnv("PRODUCTION").map(Boolean::parseBoolean).orElse(false);
  }

  default Optional<String> getOptionalEnv(String key) {
    return Optional.ofNullable(System.getenv(key));
  }

  default String getRequiredEnv(String key) {
    return getOptionalEnv(key).orElseThrow(() -> new IllegalStateException("Missing environment variable: " + key));
  }
}
