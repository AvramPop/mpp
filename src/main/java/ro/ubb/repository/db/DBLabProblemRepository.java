package ro.ubb.repository.db;

import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.db.sort.Sort;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DBLabProblemRepository extends DBRepository<Long, LabProblem> {
  public DBLabProblemRepository(String dbCredentialsFilename) {
    this(dbCredentialsFilename, "public.\"LabProblems\"");
  }

  public DBLabProblemRepository(String dbCredentialsFilename, String tableName) {
    super(dbCredentialsFilename, tableName);
  }
  /** @return all entities. Sorted by the Sort entity */
  @Override
  public Iterable<LabProblem> findAll(Sort sort) {
    Iterable<LabProblem> unsorted = findAll();
    Iterable<LabProblem> sorted = sort.sort(unsorted);
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
  public Optional<LabProblem> findOne(Long aLong) {
    if (aLong == null) throw new IllegalArgumentException("ID must not be null");
    String query =
        "select lab_problem_id, description, lab_problem_number  from "
            + this.tableName
            + " where lab_problem_id = "
            + aLong;

    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        LabProblem newLabProblem = new LabProblem();
        newLabProblem.setId(resultSet.getLong("lab_problem_id"));
        newLabProblem.setProblemNumber(resultSet.getInt("lab_problem_number"));
        newLabProblem.setDescription(resultSet.getString("description"));

        return Optional.of(newLabProblem);
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
  public Iterable<LabProblem> findAll() {
    String query = "select lab_problem_id, description, lab_problem_number  from " + this.tableName;
    Set<LabProblem> result = new HashSet<>();

    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        LabProblem newLabProblem = new LabProblem();
        newLabProblem.setId(resultSet.getLong("lab_problem_id"));
        newLabProblem.setProblemNumber(resultSet.getInt("lab_problem_number"));
        newLabProblem.setDescription(resultSet.getString("description"));
        result.add(newLabProblem);
      }
    } catch (SQLException e) {
      e.printStackTrace();
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
  public Optional<LabProblem> save(LabProblem entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null");
    }
    String query = "insert into " + this.tableName + " values (?, ?, ?)";

    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(1, entity.getId());
      statement.setString(2, entity.getDescription());
      statement.setLong(3, entity.getProblemNumber());
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
  public Optional<LabProblem> delete(Long aLong) {
    if (aLong == null) {
      throw new IllegalArgumentException("Id must not be null!");
    }
    String selectQuery =
        "select lab_problem_id, description, lab_problem_number  from "
            + this.tableName
            + "where lab_problem_id = "
            + aLong;
    String deleteQuery = "delete from " + this.tableName + " where lab_problem_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
      ResultSet selectResultSet = selectStatement.executeQuery();
      if (selectResultSet.next()) {
        LabProblem deletedLabProblem = new LabProblem();
        deletedLabProblem.setId(selectResultSet.getLong("lab_problem_id"));
        deletedLabProblem.setDescription(selectResultSet.getString("description"));
        deletedLabProblem.setProblemNumber(selectResultSet.getInt("lab_problem_number"));
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        deleteStatement.setLong(1, aLong);
        deleteStatement.executeUpdate();
        return Optional.of(deletedLabProblem);
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
  public Optional<LabProblem> update(LabProblem entity) throws ValidatorException {
    if (entity == null) {
      throw new IllegalArgumentException("entity must not be null!");
    }

    String query =
        "update "
            + this.tableName
            + " set description = ?, lab_problem_number = ? where lab_problem_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(3, entity.getId());
      statement.setString(1, entity.getDescription());
      statement.setLong(2, entity.getProblemNumber());
      if (statement.executeUpdate() == 0) return Optional.of(entity);
      return Optional.empty();
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.of(entity);
    }
  }
}
