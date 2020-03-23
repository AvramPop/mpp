package ro.ubb.service;

import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.db.sort.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabProblemService {

  private SortingRepository<Long, LabProblem> repository;
  private Validator<LabProblem> labProblemValidator;

  public LabProblemService(
      SortingRepository<Long, LabProblem> repository, Validator<LabProblem> labProblemValidator) {
    this.repository = repository;
    this.labProblemValidator = labProblemValidator;
  }

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
  public Optional<LabProblem> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    labProblemValidator.validate(newLabProblem);

    return repository.save(newLabProblem);
  }

  /**
   * Returns all the lab problems in the ro.ubb.repository in a Set
   *
   * @return a Set which stores all the lab problems
   */
  public Set<LabProblem> getAllLabProblems() {
    Iterable<LabProblem> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  /**
   * Return all Lab problems sorted by the sort criteria.
   */
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    Iterable<LabProblem> problems = repository.findAll(sort);
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
  }

  /**
   * Get Optional containing lab problem with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find lab problem by
   * @return Optional containing the sought LabProblem or null otherwise
   */
  public Optional<LabProblem> getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id);
  }

  /**
   * Deletes a lab problem from the ro.ubb.repository
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }

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
  public Optional<LabProblem> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    labProblemValidator.validate(newLabProblem);

    return repository.update(newLabProblem);
  }

  /**
   * Filters the elements of the ro.ubb.repository by a given problem number
   *
   * @param problemNumberToFilterBy the problem number to be filtered by
   * @return a {@code Set} - of entities filtered by the given problem number
   */
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    if (problemNumberToFilterBy < 0) {
      throw new IllegalArgumentException("problem number negative!");
    }
    Iterable<LabProblem> labProblems = repository.findAll();
    Set<LabProblem> filteredLabProblems = new HashSet<>();
    labProblems.forEach(filteredLabProblems::add);
    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    return filteredLabProblems;
  }
}
