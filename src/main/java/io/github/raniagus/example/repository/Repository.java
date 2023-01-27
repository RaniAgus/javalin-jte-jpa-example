package io.github.raniagus.example.repository;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import io.github.raniagus.example.model.PersistableEntity;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Repository<T extends PersistableEntity> implements WithSimplePersistenceUnit {

  public boolean existsById(Long id) {
    return getById(id).isPresent();
  }

  public Optional<T> getById(Long id) {
    if (id == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(find(getEntityClass(), id));
  }

  public Optional<T> getBy(Map.Entry<String, Object>... entries) {
    return findBy(entries).stream().findFirst();
  }

  public List<T> findAll() {
    return createQuery("from " + getEntityClass().getSimpleName(), getEntityClass())
        .getResultList();
  }

  public List<T> findBy(Map.Entry<String, Object>... entries) {
    Map<String, Object> params = Map.ofEntries(entries);
    TypedQuery<T> query = createQuery(
        "from " + getEntityClass().getSimpleName()
            + " where " + getWhereClause(params.keySet()),
        getEntityClass());
    params.forEach(query::setParameter);
    return query.getResultList();
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

  public boolean deleteById(Long id) {
    return getById(id).map(this::delete).orElse(false);
  }

  public void deleteAll() {
    findAll().forEach(this::remove);
  }

  public long count() {
    return createQuery("select count(*) from " + getEntityClass().getSimpleName(), Long.class)
        .getSingleResult();
  }

  private String getWhereClause(Set<String> params) {
    return params.stream()
        .map(param -> param + " = :" + param.replace(".", ""))
        .collect(Collectors.joining(" and "));
  }

  protected abstract Class<T> getEntityClass();
}
