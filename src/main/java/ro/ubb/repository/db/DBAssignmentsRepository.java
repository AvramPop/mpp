package ro.ubb.repository.db;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBAssignmentsRepository extends DBRepository<Long, Assignment> {
  public DBAssignmentsRepository(String dbCredentialsFilename) {
    super(dbCredentialsFilename);
  }

  @Override
  public Iterable<Assignment> findAll(Sort sort) {
    return sort.sort(findAll());
  }

  @Override
  public Optional<Assignment> findOne(Long aLong) {
    if(aLong == null) throw new NullPointerException();
    String query =
        "select assignment_id, student_id, lab_problem_id, grade from public.\"Assignments\" where assignment_id = "
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

  @Override
  public Iterable<Assignment> findAll() {
    List<Assignment> result = new ArrayList<>();
    String query = "select assignment_id, student_id, lab_problem_id, grade from public.\"Assignments\"";

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

  @Override
  public Optional<Assignment> save(Assignment entity) throws ValidatorException {
    String query = "insert into public.\"Assignments\" values (?, ?, ?, ?)";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(1, entity.getId());
      statement.setLong(2, entity.getStudentId());
      statement.setLong(3, entity.getLabProblemId());
      statement.setLong(4, entity.getGrade());
      statement.executeUpdate();
      return Optional.empty();
    } catch (SQLException e) {
      return Optional.ofNullable(entity);
    }
  }

  @Override
  public Optional<Assignment> delete(Long aLong) {
    if(aLong == null){
      throw new IllegalArgumentException("Id must not be null!");
    }
    String selectQuery =
            "select assignment_id, student_id, lab_problem_id, grade from public.\"Assignments\" where assignment_id = "
                + aLong;
    String deleteQuery = "delete from public.\"Assignments\" where assignment_id = ?";
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

  @Override
  public Optional<Assignment> update(Assignment entity) throws ValidatorException {
  String query = "update public.\"Assignments\" set student_id = ?, lab_problem_id = ?, grade = ? where assignment_id = ?";
    try (Connection connection = dbConnection()) {
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setLong(4, entity.getId());
      statement.setLong(1, entity.getStudentId());
      statement.setLong(2, entity.getLabProblemId());
      statement.setLong(3, entity.getGrade());
      statement.executeUpdate();
      return Optional.empty();
    } catch (SQLException e) {
      return Optional.of(entity);
    }

  }
}
