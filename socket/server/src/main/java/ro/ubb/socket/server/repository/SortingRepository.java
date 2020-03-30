package ro.ubb.socket.server.repository;

import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.service.sort.Sort;

import java.io.Serializable;

/** Repository which returns the elements sorted */
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
