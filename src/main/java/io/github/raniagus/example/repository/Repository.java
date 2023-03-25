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
    if (id == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(find(getEntityClass(), id));
  }

  public List<T> findAll() {
    return createQuery("from " + getEntityClass().getSimpleName(), getEntityClass())
        .getResultList();
  }

  public Optional<T> save(T entity) {
    if (existsById(entity.getId())) {
      return Optional.empty();
    }
    persist(entity);
    return Optional.of(entity);
  }

  public List<T> saveAll(List<T> entities) {
    return entities.stream()
        .map(this::save)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }

  public Optional<T> update(T entity) {
    if (!existsById(entity.getId())) {
      return Optional.empty();
    }
    return Optional.ofNullable(merge(entity));
  }

  public List<T> updateAll(List<T> entities) {
    return entities.stream()
        .map(this::update)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());
  }

  public boolean delete(T entity) {
    if (!existsById(entity.getId())) {
      return false;
    }
    remove(entity);
    return true;
  }

  public boolean deleteById(UUID id) {
    return findById(id).map(this::delete).orElse(false);
  }

  public void deleteAll() {
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
    params.forEach(query::setParameter);
    return query.getResultList();
  }

  private String getWhereClause(Set<String> params) {
    return params.stream()
        .map(param -> param + " = :" + param.replace(".", ""))
        .collect(Collectors.joining(" and "));
  }

  protected abstract Class<T> getEntityClass();
}
