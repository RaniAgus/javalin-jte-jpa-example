package io.github.raniagus.core.repository;

import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;

import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface RepositoryUtils {
  static Map<String, Object> paramsToMap(Object... args) {
    return Streams.stream(Iterables.partition(Arrays.asList(args), 2))
        .collect(HashMap::new, (map, pair) -> map.put((String) pair.get(0), pair.get(1)), HashMap::putAll);
  }

  static String getWhereClause(Set<String> params) {
    return params.stream()
        .map(param -> param + " = :" + param.replace(".", ""))
        .collect(Collectors.joining(" and "));
  }

  static <T> TypedQuery<T> setParameters(TypedQuery<T> query, Map<String, Object> params) {
    params.forEach(query::setParameter);
    return query;
  }
}
