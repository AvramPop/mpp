package ro.ubb.socket.server.repository;


import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.service.sort.Sort;

import java.io.Serializable;

/** Created by radu. */
public interface SortingRepository<ID extends Serializable, T extends BaseEntity<ID>>
    extends Repository<ID, T> {

  Iterable<T> findAll(Sort sort);
}
