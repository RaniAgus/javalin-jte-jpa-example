package io.github.raniagus.example.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public interface WithEncodingUtil {
  default String decode(String s) {
    return s == null ? null : URLDecoder.decode(s, StandardCharsets.UTF_8);
  }

  default String encode(String... strings) {
    return URLEncoder.encode(String.join("", strings), StandardCharsets.UTF_8);
  }

  default String encode(Map<String, String> params) {
    return params.entrySet().stream()
        .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
        .collect(Collectors.joining("&"));
  }
}
