package ro.ubb.remoting.server.repository.containers;

import ro.ubb.remoting.common.domain.BaseEntity;
import ro.ubb.remoting.common.service.sort.Sort;

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
