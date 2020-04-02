package ro.ubb.remoting.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.Optional;

public class LabProblemRepository implements SortingRepository<Long, LabProblem> {
  @Autowired private JdbcOperations jdbcOperations;

  @Override
  public Iterable<LabProblem> findAll(Sort sort) {
    return null;
  }

  @Override
  public Optional<LabProblem> findOne(Long aLong) {
    return Optional.empty();
  }

  @Override
  public Iterable<LabProblem> findAll() {
    return null;
  }

  @Override
  public Optional<LabProblem> save(LabProblem entity) throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Optional<LabProblem> delete(Long aLong) {
    return Optional.empty();
  }

  @Override
  public Optional<LabProblem> update(LabProblem entity) throws ValidatorException {
    return Optional.empty();
  }
}
