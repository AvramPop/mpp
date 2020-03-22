package ro.ubb.repository.db;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBStudentRepository extends DBRepository<Long, Student> {
  public DBStudentRepository(String dbCredentialsFilename) {
    super(dbCredentialsFilename);
  }

  @Override
  public Iterable<Student> findAll(Sort sort) {
    return sort.sort(this.findAll());
  }

  @Override
  public Optional<Student> findOne(Long aLong) {
    if(aLong == null)
      throw new IllegalArgumentException("ID must not be null");
    String selectQuery = "select student_id, serial_number, group_number, name from public.\"Students\" where student_id = ?";

    try(Connection connection = dbConnection())
    {
      PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = preparedStatement.executeQuery();
      if(resultSet.next()){
        Student newStudent = new Student();
        newStudent.setId(resultSet.getLong("student_id"));
        newStudent.setSerialNumber(resultSet.getString("serial_number"));
        newStudent.setName(resultSet.getString("name"));
        newStudent.setGroup(resultSet.getInt("group_number"));
        return Optional.of(newStudent);
      } else {
        return Optional.empty();
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
      return Optional.empty();
    }

  }

  @Override
  public Iterable<Student> findAll() {
    List<Student> result = new ArrayList<>();
    String selectQuery = "select student_id, serial_number, group_number, name from public.\"Students\"";

    try (Connection conn = dbConnection()){
        PreparedStatement stm = conn.prepareStatement(selectQuery);
        ResultSet rs = stm.executeQuery();

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
  public Optional<Student> save(Student entity) throws ValidatorException {
    if(entity == null){
      throw new IllegalArgumentException("entity must not be null");
    }
    String query = "insert into public.\"Students\" values (?, ?, ?, ?)";
    try(Connection connection = dbConnection()){
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(1, entity.getId());
      statement.setString(2, entity.getSerialNumber());
      statement.setInt(3, entity.getGroup());
      statement.setString(4, entity.getName());
      return Optional.empty();
    }
    catch (SQLException ex){
      return Optional.of(entity);
    }
  }

  @Override
  public Optional<Student> delete(Long aLong) {
    if(aLong == null){
      throw new IllegalArgumentException("ID must not be null");
    }
    String selectQuery = "select student_id, serial_number, group_number, name from public.\"Students\" ";
    String deleteQuery = "delete from public.\"Student\" where student_id = ?";
    try(Connection connection = dbConnection()){
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSetSelect = selectStatement.executeQuery();
      if(resultSetSelect.next()){
        Student newStudent = new Student();
        newStudent.setId(resultSetSelect.getLong("student_id"));
        newStudent.setSerialNumber(resultSetSelect.getString("serial_number"));
        newStudent.setName(resultSetSelect.getString("name"));
        newStudent.setGroup(resultSetSelect.getInt("group_number"));
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1,aLong);
        deleteStatement.executeUpdate();
        return Optional.of(newStudent);
      }
      else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public Optional<Student> update(Student entity) throws ValidatorException {
    if(entity == null){
      throw new IllegalArgumentException("entity must not be null");
    }
    String updateQuery = "update public.\"Students\" set serial_number = ?, group_number = ?, name = ? where student_id = ?";
    try(Connection connection = dbConnection()){
      PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
      preparedStatement.setLong(4,entity.getId());
      preparedStatement.setString(1,entity.getSerialNumber());
      preparedStatement.setInt(2,entity.getGroup());
      preparedStatement.setString(3,entity.getName());
      return Optional.empty();
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.of(entity);
    }
  }
}
