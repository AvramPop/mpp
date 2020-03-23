package ro.ubb.repository;

import ro.ubb.domain.BaseEntity;
import ro.ubb.repository.db.sort.Sort;

import java.io.Serializable;

/** Created by radu. */
public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>>
    extends Repository<ID, T> {

  Iterable<T> findAll(Sort sort);
}
