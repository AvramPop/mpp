package service;

import domain.LabProblem;
import domain.exceptions.ValidatorException;
import repository.Repository;

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
   * Adds a new entity to the repository, if it is correctly performed the
   *
   * @param labProblem the labProblem entity which is to be added to the repository
   * @throws ValidatorException in the case that the labProblem entity is invalid, this is verified
   *     by * the labProblem validator
   */
  public Optional<LabProblem> addLabProblem(LabProblem labProblem) throws ValidatorException {
    return repository.save(labProblem);
  }

  /**
   * Returns all the lab problems in the repository in a Set
   *
   * @return a Set which stores all the lab problems
   */
  public Set<LabProblem> getAllLabProblems() {
    Iterable<LabProblem> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  /**
   * Get Optional containing lab problem with given id if there is one in the repository below.
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
   * Deletes a lab problem from the repository
   *
   * @param id the id of the lab problem to be deleted
   */
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }

  /**
   * Updates an existing entity in the repository with the same id
   *
   * @param labProblem the lab problem to be updated
   * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist)
   *     * returns the entity.
   */
  public Optional<LabProblem> updateLabProblem(LabProblem labProblem) throws ValidatorException {

    return repository.update(labProblem);
  }

  /**
   * Filters the elements of the repository by a given problem number
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
