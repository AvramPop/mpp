package ro.ubb.remoting.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;

public class LabProblemRepository implements SortingRepository<Long, LabProblem> {
  @Autowired private JdbcOperations jdbcOperations;

  @Override
  public Iterable<LabProblem> findAll(Sort sort) {
    return sort.sort(this.findAll());
  }

  @Override
  public Optional<LabProblem> findOne(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("Id must not be null");
    String query = "select * from public.\"LabProblems\" where lab_problem_id=" + aLong;
    List<LabProblem> result = this.getLabProblems(query);
    try {
      return Optional.of(result.get(0));
    } catch (IndexOutOfBoundsException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Iterable<LabProblem> findAll() {
    String query = "SELECT * FROM public.\"LabProblems\"";
    return getLabProblems(query);
  }

  @Override
  public Optional<LabProblem> save(LabProblem entity) {
    if (entity == null) throw new IllegalArgumentException("Entity must not be null");
    String query = "insert into public.\"LabProblems\" values (?,?,?)";
    try {
      if (jdbcOperations.update(
              query, entity.getId(), entity.getDescription(), entity.getProblemNumber())
          == 1) {
        return Optional.empty();
      }
    } catch (DataAccessException e) {
      return Optional.of(entity);
    }
    return Optional.of(entity);
  }

  @Override
  public Optional<LabProblem> delete(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("Id must not be null");
    String query = "delete from public.\"LabProblems\" where lab_problem_id = " + aLong;
    Optional<LabProblem> labProblem = this.findOne(aLong);
    labProblem.ifPresent((value) -> jdbcOperations.update(query, aLong));
    return labProblem;
  }

  @Override
  public Optional<LabProblem> update(LabProblem entity) {
    if (entity == null) throw new IllegalArgumentException("Entity must not be null");
    String query =
        "update public.\"LabProblems\" set description = ?, lab_problem_number = ? where lab_problem_id = ?";
    if (jdbcOperations.update(
            query, entity.getDescription(), entity.getProblemNumber(), entity.getId())
        == 0) {
      return Optional.of(entity);
    } else {
      return Optional.empty();
    }
  }

  private List<LabProblem> getLabProblems(String sql) {
    return jdbcOperations.query(
        sql,
        (rs, rowNum) -> {
          Long id = rs.getLong("lab_problem_id");
          String description = rs.getString("description");
          int problemNumber = rs.getInt("lab_problem_number");
          LabProblem labProblem = new LabProblem(problemNumber, description);
          labProblem.setId(id);
          return labProblem;
        });
  }
}
