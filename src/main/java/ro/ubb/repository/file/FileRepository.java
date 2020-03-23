package ro.ubb.repository.file;

import ro.ubb.domain.BaseEntity;
import ro.ubb.domain.ObjectFromFileLine;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.db.sort.Sort;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Non-volatile generic data ro.ubb.repository holding everything in a file specified in the
 * constructor if the filename is empty it generates a default file
 *
 * @param <ID> type of the id of given entity to store
 * @param <T> type of entity to store
 */
public class FileRepository<ID extends Serializable, T extends BaseEntity<ID>>
    implements SortingRepository<ID, T> {

  private String filename;
  private String delimiter;
  private ObjectFromFileLine<T> converterFunction;

  public FileRepository(
      String filename, String delimiter, ObjectFromFileLine<T> converterFunction) {
    this.filename = filename;
    this.delimiter = delimiter;
    this.converterFunction = converterFunction;
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

    Map<ID, T> entities =
        StreamSupport.stream(loadData().spliterator(), false)
            .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));

    return Optional.ofNullable(entities.get(id));
  }

  /** @return all entities. */
  @Override
  public Iterable<T> findAll() {
    return StreamSupport.stream(loadData().spliterator(), false).collect(Collectors.toSet());
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

    Map<ID, T> entities =
        StreamSupport.stream(loadData().spliterator(), false)
            .collect(Collectors.toMap(BaseEntity::getId, elem -> elem));
    Optional<T> optional = Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));

    if (optional.isPresent()) return optional;

    saveToFile(entity);
    return Optional.empty();
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
    Map<ID, T> entities =
        StreamSupport.stream(loadData().spliterator(), false)
            .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));
    Optional<T> optional = Optional.ofNullable(entities.remove(id));

    if (optional.isEmpty()) return Optional.empty();

    saveToFile(entities.values());
    return optional;
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

    Map<ID, T> entities =
        StreamSupport.stream(loadData().spliterator(), false)
            .collect(Collectors.toMap(elem -> elem.getId(), elem -> elem));

    Optional<T> optional =
        Optional.ofNullable(
            entities.computeIfPresent(entity.getId(), (k, v) -> entity) == null ? entity : null);
    if (optional.isPresent()) return optional;

    saveToFile(entities.values());
    return Optional.empty();
  }

  /**
   * Loads the data from the file
   *
   * @return all entitites
   */
  private Iterable<T> loadData() {
    Path path = Paths.get(this.filename);
    HashSet<T> newEntities = new HashSet<>();
    try {
      Files.lines(path)
          .forEach(line -> newEntities.add(converterFunction.convert(line, delimiter)));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return newEntities;
  }

  /**
   * Saves an entity to the file
   *
   * @param entitiesToSave the entities to be saved on the file
   */
  private void saveToFile(Collection<T> entitiesToSave) {
    Path path = Paths.get(this.filename);
    List<String> entityLines =
        entitiesToSave.stream()
            .map(elem -> elem.objectToFileLine(delimiter))
            .collect(Collectors.toList());
    try {
      Files.write(path, entityLines);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Saves an entity to the file
   *
   * @param entity the entity to be saved on the list
   */
  private void saveToFile(T entity) {
    Path path = Paths.get(this.filename);
    try {
      Files.write(
          path, (entity.objectToFileLine(delimiter) + "\n").getBytes(), StandardOpenOption.APPEND);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
