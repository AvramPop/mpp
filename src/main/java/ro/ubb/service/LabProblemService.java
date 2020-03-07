package ro.ubb.service;

import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabProblemService {

  private Repository<Long, LabProblem> repository;

  public LabProblemService(Repository<Long, LabProblem> repository) {
    this.repository = repository;
  }

  /**
   * Adds a lab problem inside the ro.ubb.repository
   * @param id id number of entity to be updated
   * @param problemNumber problem number of entity to be updated
   * @param description description of entity to be updated
   * @return an {@code Optional} containing the null if successfully added or the passed entity otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  public Optional<LabProblem> addLabProblem(Long id, int problemNumber, String description) throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);
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
   * Get Optional containing lab problem with given id if there is one in the ro.ubb.repository below.
   * @param id to find lab problem by
   * @return Optional containing the sought LabProblem or null otherwise
   */
  public Optional<LabProblem> getLabProblemById(Long id) {
    if(id == null || id < 0) {
      throw new IllegalArgumentException("negative id!");
    }
    return repository.findOne(id);
  }

  /**
   * Deletes a lab problem from the ro.ubb.repository
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity passed to the repository
   */
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }

  /**
   * Updates a lab problem inside the ro.ubb.repository
   * @param id id number of entity to be updated
   * @param problemNumber problem number of entity to be updated
   * @param description description of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  public Optional<LabProblem> updateLabProblem(Long id, int problemNumber, String description) throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);
    return repository.update(newLabProblem);
  }

  /**
   * Filters the elements of the ro.ubb.repository by a given problem number
   *
   * @param problemNumberToFilterBy the problem number to be filtered by
   * @return a {@code Set} - of entities filtered by the given problem number
   */
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    if(problemNumberToFilterBy < 0){
      throw new IllegalArgumentException("problem number negative!");
    }
    Iterable<LabProblem> labProblems = repository.findAll();
    Set<LabProblem> filteredLabProblems = new HashSet<>();
    labProblems.forEach(filteredLabProblems::add);
    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    return filteredLabProblems;
  }
}
