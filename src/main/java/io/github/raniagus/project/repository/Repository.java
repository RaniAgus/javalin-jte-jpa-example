package io.github.raniagus.project.repository;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.github.raniagus.project.model.PersistableEntity;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.raniagus.project.repository.RepositoryUtils.getWhereClause;
import static io.github.raniagus.project.repository.RepositoryUtils.paramsToMap;
import static io.github.raniagus.project.repository.RepositoryUtils.setParameters;

public interface Repository<T extends PersistableEntity> extends WithSimplePersistenceUnit {

  default boolean existsById(Long id) {
    return getById(id).isPresent();
  }

  default Optional<T> getById(Long id) {
    if (id == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(find(getEntityClass(), id));
  }

  default Optional<T> getBy(Object... keyValuePairs) {
    return findBy(keyValuePairs).stream().findFirst();
  }

  default List<T> findAll() {
    return createQuery("from " + getEntityClass().getSimpleName(), getEntityClass())
        .getResultList();
  }

  default List<T> findBy(Object... keyValuePairs) {
    Map<String, Object> params = paramsToMap(keyValuePairs);
    TypedQuery<T> query = createQuery(
        "from " + getEntityClass().getSimpleName()
            + " where " + getWhereClause(params.keySet()),
        getEntityClass());

    return setParameters(query, params).getResultList();
  }

  default Optional<T> save(T entity) {
    if (existsById(entity.getId())) {
      return Optional.empty();
    }
    persist(entity);
    return Optional.of(entity);
  }

  default Optional<T> update(T entity) {
    if (!existsById(entity.getId())) {
      return Optional.empty();
    }
    return Optional.ofNullable(merge(entity));
  }

  default boolean delete(T entity) {
    if (!existsById(entity.getId())) {
      return false;
    }
    remove(entity);
    return true;
  }

  default boolean deleteById(Long id) {
    return getById(id).map(this::delete).orElse(false);
  }

  default void deleteAll() {
    findAll().forEach(this::remove);
  }

  default long count() {
    return createQuery("select count(*) from " + getEntityClass().getSimpleName(), Long.class)
        .getSingleResult();
  }

  Class<T> getEntityClass();
}
