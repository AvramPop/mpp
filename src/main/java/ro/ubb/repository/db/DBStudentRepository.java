package ro.ubb.repository.db;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.db.sort.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DBStudentRepository extends DBRepository<Long, Student> {
  public DBStudentRepository(String dbCredentialsFilename) {

    this(dbCredentialsFilename, "public.\"Students\"");
  }

  public DBStudentRepository(String dbCredentialsFilename, String tableName) {

    super(dbCredentialsFilename, tableName);
  }

  @Override
  public Iterable<Student> findAll(Sort sort) {
    Iterable<Student> unsorted = findAll();
    Iterable<Student> sorted = sort.sort(unsorted);
    return sorted;
  }
  /**
   * Find the entity with the given {@code id}.
   *
   * @param aLong must be not null.
   * @return an {@code Optional} encapsulating the entity with the given id.
   * @throws IllegalArgumentException if the given id is null.
   */
  @Override
  public Optional<Student> findOne(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("ID must not be null");
    String selectQuery =
        "select student_id, serial_number, group_number, name from "
            + this.tableName
            + " where student_id = "
            + aLong;

    try (Connection connection = dbConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        Student newStudent = new Student();
        newStudent.setId(resultSet.getLong("student_id"));
        newStudent.setSerialNumber(resultSet.getString("serial_number"));
        newStudent.setName(resultSet.getString("name"));
        newStudent.setGroup(resultSet.getInt("group_number"));
        return Optional.of(newStudent);
      } else {
        return Optional.empty();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      return Optional.empty();
    }
  }
  /** @return all entities. */
  @Override
  public Iterable<Student> findAll() {
    Set<Student> result = new HashSet<>();
    String selectQuery =
        "select student_id, serial_number, group_number, name from " + this.tableName;

    try (Connection conn = dbConnection()) {
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
  /**
   * Saves the given entity.
   *
   * @param entity must not be null.
   * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists)
   *     returns the entity.
   * @throws IllegalArgumentException if the given entity is null.
   * @throws ValidatorException if the entity is not valid.
   */
  @Override
  public Optional<Student> save(Student entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    String query = "insert into " + this.tableName + " values (?, ?, ?, ?)";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(1, entity.getId());
      statement.setString(2, entity.getSerialNumber());
      statement.setInt(3, entity.getGroup());
      statement.setString(4, entity.getName());
      statement.executeUpdate();
      return Optional.empty();
    } catch (SQLException ex) {
      return Optional.of(entity);
    }
  }
  /**
   * Removes the entity with the given id.
   *
   * @param aLong must not be null.
   * @return an {@code Optional} - null if there is no entity with the given id, otherwise the
   *     removed entity.
   * @throws IllegalArgumentException if the given id is null.
   */
  @Override
  public Optional<Student> delete(Long aLong) {
    if (aLong == null) {
      throw new IllegalArgumentException("ID must not be null");
    }
    String selectQuery =
        "select student_id, serial_number, group_number, name from "
            + this.tableName
            + "where student_id = "
            + aLong;
    String deleteQuery = "delete from " + this.tableName + " where student_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet resultSetSelect = selectStatement.executeQuery();
      if (resultSetSelect.next()) {
        Student newStudent = new Student();
        newStudent.setId(resultSetSelect.getLong("student_id"));
        newStudent.setSerialNumber(resultSetSelect.getString("serial_number"));
        newStudent.setName(resultSetSelect.getString("name"));
        newStudent.setGroup(resultSetSelect.getInt("group_number"));
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1, aLong);
        deleteStatement.executeUpdate();
        return Optional.of(newStudent);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }
  /**
   * Updates the given entity.
   *
   * @param entity must not be null.
   * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist)
   *     returns the entity.
   * @throws IllegalArgumentException if the given entity is null.
   * @throws ValidatorException if the entity is not valid.
   */
  @Override
  public Optional<Student> update(Student entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    String updateQuery =
        "update "
            + this.tableName
            + " set serial_number = ?, group_number = ?, name = ? where student_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
      preparedStatement.setLong(4, entity.getId());
      preparedStatement.setString(1, entity.getSerialNumber());
      preparedStatement.setInt(2, entity.getGroup());
      preparedStatement.setString(3, entity.getName());
      if (preparedStatement.executeUpdate() == 0) return Optional.of(entity);
      return Optional.empty();
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.of(entity);
    }
  }
}
