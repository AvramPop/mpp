package ro.ubb.repository.db;

import ro.ubb.domain.BaseEntity;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Created by radu. */
public class Sort {
  public <T extends BaseEntity<Long>> Iterable<T> sort (Iterable<T> iterableToSort) {
    return StreamSupport.stream(iterableToSort.spliterator(), false)
        .sorted(Comparator.comparing(BaseEntity::getId))
        .collect(Collectors.toList());
  }
}
