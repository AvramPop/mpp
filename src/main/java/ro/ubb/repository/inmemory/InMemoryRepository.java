package ro.ubb.repository.inmemory;

import ro.ubb.domain.BaseEntity;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.db.sort.Sort;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 * Volatile generic data ro.ubb.repository holding everything in memory.
 *
 * @param <ID> type of the id of given entity to store
 * @param <T> type of entity to store
 */
public class InMemoryRepository<ID extends Serializable, T extends BaseEntity<ID>>
    implements SortingRepository<ID, T> {

  private Map<ID, T> entities;

  public InMemoryRepository() {
    entities = new HashMap<>();
  }
  /**
   * Find the entity with the given {@code id}.
   *
   * @param id must be not null.
   * @return an {@code Optional} encapsulating the entity with the given id.
   * @throws IllegalArgumentException if the given id is null.
   */
  @Override
  public Optional<T> findOne(ID id) {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    return Optional.ofNullable(entities.get(id));
  }

  /** @return all entities */
  @Override
  public Iterable<T> findAll() {
    return new HashSet<>(entities.values());
  }

  /**
   * Saves the given entity.
   *
   * @param entity must not be null.
   * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists)
   *     returns the entity.
   * @throws IllegalArgumentException if the given entity is null.
   * @throws ValidatorException if the entity is not valid.
   */
  @Override
  public Optional<T> save(T entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("id must not be null");
    }

    return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
  }

  /**
   * Removes the entity with the given id.
   *
   * @param id must not be null.
   * @return an {@code Optional} - null if there is no entity with the given id, otherwise the
   *     removed entity.
   * @throws IllegalArgumentException if the given id is null.
   */
  @Override
  public Optional<T> delete(ID id) {
    if (id == null) {
      throw new IllegalArgumentException("id must not be null");
    }
    return Optional.ofNullable(entities.remove(id));
  }

  /**
   * Updates the given entity.
   *
   * @param entity must not be null.
   * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist)
   *     returns the entity.
   * @throws IllegalArgumentException if the given entity is null.
   * @throws ValidatorException if the entity is not valid.
   */
  @Override
  public Optional<T> update(T entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }

    return Optional.ofNullable(
        entities.computeIfPresent(entity.getId(), (k, v) -> entity) == null ? entity : null);
  }
  /**
   * Return all entities sorted by the sort criterie.
   */
  @Override
  public Iterable<T> findAll(Sort sort) {
    Iterable<T> unsorted = findAll();
    Iterable<T> sorted = sort.sort(unsorted);
    return sorted;
  }
}
