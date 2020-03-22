package ro.ubb.repository.db;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DBLabProblemRepository extends DBRepository<Long, LabProblem> {
    public DBLabProblemRepository(String dbCredentialsFilename) {
        super(dbCredentialsFilename);
    }

    @Override
    public Iterable<LabProblem> findAll(Sort sort) {
        return sort.sort(this.findAll());
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
        if(aLong == null) throw new IllegalArgumentException("ID must not be null");
        String query =
                "select lab_problem_id, description, lab_problem_number  from public.\"LabProblems\" where lab_problem_id = "
                        + aLong;

        try (Connection connection = dbConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                LabProblem newLabProblem = new LabProblem();
                newLabProblem.setId(resultSet.getLong("assignment_id"));
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

    /**
     * @return all entities.
     */
    @Override
    public Iterable<LabProblem> findAll() {
        String query =
                "select lab_problem_id, description, lab_problem_number  from public.\"LabProblems\"";
        List<LabProblem> result = new ArrayList<>();

        try (Connection connection = dbConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LabProblem newLabProblem = new LabProblem();
                newLabProblem.setId(resultSet.getLong("assignment_id"));
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
     * returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<LabProblem> save(LabProblem entity) throws ValidatorException {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        String query = "insert into public.\"LabProblems\" values (?, ?, ?)";

        try(Connection connection = dbConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getProblemNumber());
            statement.executeUpdate();
            return Optional.empty();

        }
        catch (SQLException ex){
            return Optional.of(entity);
        }

    }

    /**
     * Removes the entity with the given id.
     *
     * @param aLong must not be null.
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the
     * removed entity.
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public Optional<LabProblem> delete(Long aLong) {
        if(aLong == null){
            throw new IllegalArgumentException("Id must not be null!");
        }
        String selectQuery =
                "select lab_problem_id, description, lab_problem_number  from public.\"LabProblems\" where lab_problem_id = "
                        + aLong;
        String deleteQuery = "delete from public.\"LabProblems\" where lab_problem_id = ?";
        try (Connection connection = dbConnection()) {
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            ResultSet selectResultSet = selectStatement.executeQuery();
            if (selectResultSet.next()) {
                LabProblem deletedAssignment = new LabProblem();
                deletedAssignment.setId(selectResultSet.getLong("assignment_id"));
                deletedAssignment.setDescription(selectResultSet.getString("description"));
                deletedAssignment.setProblemNumber(selectResultSet.getInt("lab_problem_number"));
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
     * returns the entity.
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidatorException       if the entity is not valid.
     */
    @Override
    public Optional<LabProblem> update(LabProblem entity) throws ValidatorException {
        if(entity == null){
            throw new IllegalArgumentException("entity must not be null!");
        }

        String query = "update public.\"LabProblems\" set description = ?, lab_problem_number = ? where lab_problem_id = ?";
        try (Connection connection = dbConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(4, entity.getId());
            statement.setString(1, entity.getDescription());
            statement.setLong(2, entity.getProblemNumber());
            statement.executeUpdate();
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.of(entity);
        }
    }
}
