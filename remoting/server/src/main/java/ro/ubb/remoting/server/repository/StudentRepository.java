package ro.ubb.remoting.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;

public class StudentRepository implements SortingRepository<Long, Student> {
  @Autowired private JdbcOperations jdbcOperations;

  @Override
  public Iterable<Student> findAll() {
    String sql = "SELECT * FROM public.\"Students\"";
    return getStudents(sql);
  }

  @Override
  public Optional<Student> findOne(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("Id must not be null");
    String query = "select * from public.\"LabProblems\" where lab_problem_id=" + aLong;
    List<Student> result = this.getStudents(query);
    try {
      return Optional.of(result.get(0));
    } catch (IndexOutOfBoundsException ex) {
      return Optional.empty();
    }
  }

  @Override
  public Iterable<Student> findAll(Sort sort) {
    String sql = "SELECT * FROM public.\"Students\"";
    return sort.sort(getStudents(sql));
  }

  @Override
  public Optional<Student> save(Student entity) {
    if (entity == null) throw new IllegalArgumentException("Entity must not be null");
    String query = "insert into public.\"Students\" values (?,?,?,?)";
    try {
      if (jdbcOperations.update(
              query, entity.getId(), entity.getSerialNumber(), entity.getGroup(), entity.getName())
          == 1) {
        return Optional.empty();
      }
    } catch (DataAccessException e) {
      return Optional.of(entity);
    }
    return Optional.of(entity);
  }

  @Override
  public Optional<Student> delete(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("Id must not be null");
    String query = "delete from public.\"Students\" where student_id = " + aLong;
    Optional<Student> student = this.findOne(aLong);
    student.ifPresent((value) -> jdbcOperations.update(query, aLong));
    return student;
  }

  @Override
  public Optional<Student> update(Student entity) {
    if (entity == null) throw new IllegalArgumentException("Entity must not be null");
    String query =
        "update public.\"Students\" set serial_number = ?, group_number = ?, name = ? where student_id = ?";
    if (jdbcOperations.update(
            query, entity.getSerialNumber(), entity.getGroup(), entity.getName(), entity.getId())
        == 0) {
      return Optional.of(entity);
    } else {
      return Optional.empty();
    }
  }

  private List<Student> getStudents(String sql) {
    return jdbcOperations.query(
        sql,
        (rs, rowNum) -> {
          Long id = rs.getLong("student_id");
          String serialNumber = rs.getString("serial_number");
          int groupNumber = rs.getInt("group_number");
          String name = rs.getString("name");
          Student student = new Student(serialNumber, name, groupNumber);
          student.setId(id);
          return student;
        });
  }
}
