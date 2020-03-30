package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public interface LabProblemService {
  Future<Boolean> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException;

  Future<Set<LabProblem>> getAllLabProblems();

  /** Return all LabProblems sorted by the sort criteria. */
  Future<List<LabProblem>> getAllLabProblemsSorted(Sort sort);

  /**
   * Get Optional containing labProblem with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find labProblem by
   * @return Optional containing the sought LabProblem or null otherwise
   */
  Future<LabProblem> getLabProblemById(Long id);

  /**
   * Updates an labProblem inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  Future<Boolean> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException;

  /**
   * Deletes a lab problem from the ro.ubb.repository and also deletes all assignments corresponding
   * to that student
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Future} - null if there is no entity with the given id, otherwise the removed
   *     entity.
   */
  Future<Boolean> deleteLabProblem(Long id);
}
