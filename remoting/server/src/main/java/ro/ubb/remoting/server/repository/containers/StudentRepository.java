package ro.ubb.remoting.server.repository.containers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.service.sort.Sort;
import sun.security.validator.ValidatorException;

import java.util.Optional;

public class StudentRepository implements SortingRepository<Long, Student>{
  @Autowired
  private JdbcOperations jdbcOperations;

  @Override
  public Iterable<Student> findAll() {
    String sql = "SELECT * FROM public.\"Students\"";
    return jdbcOperations.query(sql, (rs, rowNum) -> {
      Long id = rs.getLong("student_id");
      String name = rs.getString("name");
      String serialNumber = rs.getString("serial_number");
      int group = rs.getInt("group_number");
      Student student = new Student(serialNumber, name, group);
      student.setId(id);
      return student;
    });
  }

  @Override
  public Optional<Student> findOne(Long aLong){
    return Optional.empty();
  }

  @Override
  public Iterable<Student> findAll(Sort sort){
    return null;
  }

  @Override
  public Optional<Student> save(Student entity) throws ValidatorException{
    return Optional.empty();
  }

  @Override
  public Optional<Student> delete(Long aLong){
    return Optional.empty();
  }

  @Override
  public Optional<Student> update(Student entity) throws ValidatorException{
    return Optional.empty();
  }


}
