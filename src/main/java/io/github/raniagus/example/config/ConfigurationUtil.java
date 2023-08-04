package io.github.raniagus.example.config;

import java.util.Optional;

public abstract class ConfigurationUtil {
    private ConfigurationUtil() {
    }

    public static boolean isProduction() {
        return getOptionalEnv("PRODUCTION").map(Boolean::parseBoolean).orElse(false);
    }

    protected static Optional<String> getOptionalEnv(String key) {
        return Optional.ofNullable(System.getenv(key));
    }

    protected static String getRequiredEnv(String key) {
        return getOptionalEnv(key).orElseThrow(() -> new IllegalStateException("Missing environment variable: " + key));
    }
}
