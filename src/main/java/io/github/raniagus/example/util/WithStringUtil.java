package io.github.raniagus.example.util;

import com.google.common.base.Splitter;
import java.util.List;

public interface WithStringUtil {
  default List<String> split(String string, String separator) {
    return Splitter.on(separator).omitEmptyStrings().splitToList(string);
  }
}
