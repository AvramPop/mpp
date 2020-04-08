package ro.ubb.repository;

import ro.ubb.domain.BaseEntity;
import ro.ubb.repository.sort.Sort;

import java.io.Serializable;

public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>>
    extends Repository<ID, T> {
  /**
   * The sorting entity used for sorting
   *
   * @param sort the sorting entity
   * @return the resulting iterable collection
   */
  Iterable<T> findAll(Sort sort);
}
