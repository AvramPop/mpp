package ro.ubb.repository.db;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBStudentRepository extends DBRepository<Long, Student> {
  public DBStudentRepository(String dbCredentialsFilename) {
    super(dbCredentialsFilename);
  }

  @Override
  public Iterable<Student> findAll(Sort sort) {
    return null;
  }

  @Override
  public Optional<Student> findOne(Optional<Long> aLong) {
    return Optional.empty();
  }

  @Override
  public Iterable<Student> findAll() {
    List<Student> result = new ArrayList<>();

    try (Connection conn = dbConnection();
        Statement stm = conn.createStatement();
        ResultSet rs =
            stm.executeQuery(
                "select student_id, serial_number, group_number, name from public.\"Students\"")) {

      while (rs.next()) {
        Student st = new Student();

        st.setId(rs.getLong("student_id"));
        st.setSerialNumber(rs.getString("serial_number"));
        st.setGroup(rs.getInt("group_number"));
        st.setName(rs.getString("name"));

        result.add(st);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return result;
  }

  @Override
  public Optional<Student> save(Optional<Student> entity) throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Optional<Student> delete(Optional<Long> aLong) {
    return Optional.empty();
  }

  @Override
  public Optional<Student> update(Optional<Student> entity) throws ValidatorException {
    return Optional.empty();
  }
}
