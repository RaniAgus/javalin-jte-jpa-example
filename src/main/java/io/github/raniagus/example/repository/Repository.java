package io.github.raniagus.example.repository;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.example.model.PersistableEntity;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;

public abstract class Repository<T extends PersistableEntity> implements WithSimplePersistenceUnit {
  public boolean existsById(UUID id) {
    return findById(id).isPresent();
  }

  public Optional<T> findById(UUID id) {
    return Optional.ofNullable(id).map(uuid -> find(getEntityClass(), uuid));
  }

  public List<T> findAll() {
    return createQuery("from " + getEntityClass().getSimpleName(), getEntityClass())
        .getResultList();
  }

  public Optional<T> save(T entity) {
    return Optional.of(entity)
        .filter(e -> !existsById(e.getId()))
        .map(e -> {
          persist(e);
          return e;
        });
  }

  public List<T> saveAll(List<T> entities) {
    return entities.stream()
        .map(this::save)
        .flatMap(Optional::stream)
        .toList();
  }

  public Optional<T> update(T entity) {
    return Optional.of(entity)
        .filter(e -> existsById(e.getId()))
        .map(this::merge);
  }

  public List<T> updateAll(List<T> entities) {
    return entities.stream()
        .map(this::update)
        .flatMap(Optional::stream)
        .toList();
  }

  public void removeById(UUID id) {
    findById(id).ifPresent(this::remove);
  }

  public void removeAll() {
    findAll().forEach(this::remove);
  }

  public long count() {
    return createQuery("select count(*) from " + getEntityClass().getSimpleName(), Long.class)
        .getSingleResult();
  }

  @SafeVarargs
  protected final Optional<T> findBy(Map.Entry<String, Object>... entries) {
    return findAllBy(entries).stream().findAny();
  }

  @SafeVarargs
  protected final List<T> findAllBy(Map.Entry<String, Object>... entries) {
    Map<String, Object> params = Map.ofEntries(entries);
    TypedQuery<T> query = createQuery(
        "from " + getEntityClass().getSimpleName()
            + " where " + getWhereClause(params.keySet()),
        getEntityClass());
    params.forEach((param, value) -> query.setParameter(param.replace(".", ""), value));
    return query.getResultList();
  }

  private String getWhereClause(Set<String> params) {
    return params.stream()
        .map(param -> param + " = :" + param.replace(".", ""))
        .collect(Collectors.joining(" and "));
  }

  protected abstract Class<T> getEntityClass();
}
