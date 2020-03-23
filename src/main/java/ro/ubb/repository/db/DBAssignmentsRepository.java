package ro.ubb.repository.db;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.db.sort.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DBAssignmentsRepository extends DBRepository<Long, Assignment> {
  public DBAssignmentsRepository(String dbCredentialsFilename) {

    super(dbCredentialsFilename, "public.\"Assignments\"");
  }

  public DBAssignmentsRepository(String dbCredentialsFilename, String tableName) {

    super(dbCredentialsFilename, tableName);
  }
  /** @return all entities. Sorted by the Sort entity */
  @Override
  public Iterable<Assignment> findAll(Sort sort) {
    Iterable<Assignment> unsorted = findAll();
    Iterable<Assignment> sorted = sort.sort(unsorted);
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
  public Optional<Assignment> findOne(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException();
    String query =
        "select assignment_id, student_id, lab_problem_id, grade from "
            + this.tableName
            + " where assignment_id = "
            + aLong;

    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        Assignment newAssignment = new Assignment();
        newAssignment.setId(resultSet.getLong("assignment_id"));
        newAssignment.setStudentId(resultSet.getLong("student_id"));
        newAssignment.setLabProblemId(resultSet.getLong("lab_problem_id"));
        newAssignment.setGrade(resultSet.getInt("grade"));
        return Optional.of(newAssignment);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  /** @return all entities. */
  @Override
  public Iterable<Assignment> findAll() {
    Set<Assignment> result = new HashSet<>();
    String query = "select assignment_id, student_id, lab_problem_id, grade from " + this.tableName;

    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        Assignment newAssignment = new Assignment();
        newAssignment.setId(resultSet.getLong("assignment_id"));
        newAssignment.setStudentId(resultSet.getLong("student_id"));
        newAssignment.setLabProblemId(resultSet.getLong("lab_problem_id"));
        newAssignment.setGrade(resultSet.getInt("grade"));
        result.add(newAssignment);
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
  public Optional<Assignment> save(Assignment entity) throws ValidatorException {
    if (entity == null) throw new IllegalArgumentException("entity must not be null");
    String query = "insert into " + this.tableName + " values (?, ?, ?, ?)";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(1, entity.getId());
      statement.setLong(2, entity.getStudentId());
      statement.setLong(3, entity.getLabProblemId());
      statement.setLong(4, entity.getGrade());
      statement.executeUpdate();
      return Optional.empty();
    } catch (SQLException e) {
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
  public Optional<Assignment> delete(Long aLong) {
    if (aLong == null) {
      throw new IllegalArgumentException("Id must not be null!");
    }
    String selectQuery =
        "select assignment_id, student_id, lab_problem_id, grade from "
            + this.tableName
            + " where assignment_id = "
            + aLong;
    String deleteQuery = "delete from " + this.tableName + " where assignment_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet selectResultSet = selectStatement.executeQuery();
      if (selectResultSet.next()) {
        Assignment deletedAssignment = new Assignment();
        deletedAssignment.setId(selectResultSet.getLong("assignment_id"));
        deletedAssignment.setStudentId(selectResultSet.getLong("student_id"));
        deletedAssignment.setLabProblemId(selectResultSet.getLong("lab_problem_id"));
        deletedAssignment.setGrade(selectResultSet.getInt("grade"));
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1, aLong);
        deleteStatement.executeUpdate();
        return Optional.of(deletedAssignment);
      } else {
        return Optional.empty();
      }
    } catch (SQLException e) {
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
  public Optional<Assignment> update(Assignment entity) throws ValidatorException {
    if (entity == null) throw new IllegalArgumentException("entity must not be null");
    String query =
        "update "
            + this.tableName
            + "set student_id = ?, lab_problem_id = ?, grade = ? where assignment_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(4, entity.getId());
      statement.setLong(1, entity.getStudentId());
      statement.setLong(2, entity.getLabProblemId());
      statement.setLong(3, entity.getGrade());
      if (statement.executeUpdate() == 0) return Optional.of(entity);
      return Optional.empty();
    } catch (SQLException e) {
      return Optional.of(entity);
    }
  }
}
