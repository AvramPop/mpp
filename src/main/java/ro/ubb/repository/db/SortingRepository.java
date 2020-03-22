package ro.ubb.repository.db;

import ro.ubb.domain.BaseEntity;
import ro.ubb.repository.Repository;

import java.io.Serializable;

/** Created by radu. */
public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>>
    extends Repository<ID, T> {

  Iterable<T> findAll(Sort sort);
}
