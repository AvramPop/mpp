package ro.ubb.remoting.common.service;

import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Set;

public interface LabProblemService {
  /**
   * Adds a lab problem inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param problemNumber problem number of entity to be updated
   * @param description description of entity to be updated
   * @return an {@code Optional} containing the null if successfully added or the passed entity
   *     otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  LabProblem addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException;

  /**
   * Returns all the lab problems in the ro.ubb.repository in a Set
   *
   * @return a Set which stores all the lab problems
   */
  Set<LabProblem> getAllLabProblems();

  /** Return all Lab problems sorted by the sort criteria. */
  List<LabProblem> getAllLabProblemsSorted(Sort sort);

  /**
   * Get Optional containing lab problem with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find lab problem by
   * @return Optional containing the sought LabProblem or null otherwise
   */
  LabProblem getLabProblemById(Long id);

  /**
   * Deletes a lab problem from the ro.ubb.repository
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  LabProblem deleteLabProblem(Long id);

  /**
   * Updates a lab problem inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param problemNumber problem number of entity to be updated
   * @param description description of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  LabProblem updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException;

  /**
   * Filters the elements of the ro.ubb.repository by a given problem number
   *
   * @param problemNumberToFilterBy the problem number to be filtered by
   * @return a {@code Set} - of entities filtered by the given problem number
   */
  Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy);
}
