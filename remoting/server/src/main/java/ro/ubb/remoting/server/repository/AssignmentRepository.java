package ro.ubb.remoting.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;

public class AssignmentRepository implements SortingRepository<Long, Assignment>{
  @Autowired
  private JdbcOperations jdbcOperations;
  @Override
  public Iterable<Assignment> findAll(Sort sort){
    Iterable<Assignment> unsorted = findAll();
    return sort.sort(unsorted);
  }

  @Override
  public Optional<Assignment> findOne(Long aLong){
    if (aLong == null) throw new IllegalArgumentException();
    String sql = "SELECT * FROM public.\"Assignments\" where assignment_id = " + aLong;
    List<Assignment> result = getAssignments(sql);
    if(result.size() > 0) {
      return Optional.of(result.get(0));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Iterable<Assignment> findAll(){
    String sql = "SELECT * FROM public.\"Assignments\"";
    return getAssignments(sql);
  }

  private List<Assignment> getAssignments(String sql){
    return jdbcOperations.query(sql, (rs, rowNum) -> {
      Long id = rs.getLong("assignment_id");
      Long studentId = rs.getLong("student_id");
      Long labProblemId = rs.getLong("lab_problem_id");
      int grade = rs.getInt("grade");
      Assignment assignment = new Assignment(studentId, labProblemId, grade);
      assignment.setId(id);
      return assignment;
    });
  }

  @Override
  public Optional<Assignment> save(Assignment entity){
    if (entity == null) throw new IllegalArgumentException("entity must not be null");
    String sql =
        "insert into public.\"Assignments\" (assignment_id, student_id, lab_problem_id, grade) values (?,?,?,?)";
    try{
      if(jdbcOperations.update(sql, entity.getId(), entity.getStudentId(), entity.getLabProblemId(), entity.getGrade()) == 1){
        return Optional.empty();
      }
    } catch(DataAccessException e){
      return Optional.of(entity);
    }
    return Optional.of(entity);
  }

  @Override
  public Optional<Assignment> delete(Long aLong){
    if (aLong == null) {
      throw new IllegalArgumentException("Id must not be null!");
    }
    Optional<Assignment> deleted = findOne(aLong);
    if (deleted.isPresent()) {
      String deleteQuery = "delete from public.\"Assignments\" where assignment_id = ?";
      jdbcOperations.update(deleteQuery, aLong);
      return deleted;
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Assignment> update(Assignment entity){
    if (entity == null) throw new IllegalArgumentException("entity must not be null");
    String query =
        "update public.\"Assignments\" set student_id = ?, lab_problem_id = ?, grade = ? where assignment_id = ?";
    if(jdbcOperations.update(query, entity.getStudentId(), entity.getLabProblemId(),
        entity.getGrade(), entity.getId()) == 0){
      return Optional.of(entity);
    } else {
      return Optional.empty();
    }
  }
}
