package ro.ubb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.LabProblemRepository;
import ro.ubb.service.validators.Validator;
import ro.ubb.repository.sort.Sort;

import javax.persistence.Temporal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LabProblemServiceImplementation implements LabProblemService {
    private static final Logger log = LoggerFactory.getLogger(LabProblemServiceImplementation.class);
  @Autowired private LabProblemRepository repository;
  @Autowired private Validator<LabProblem> labProblemValidator;

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
  @Override
  public LabProblem addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    log.trace("addLabProblem - method entered: id={}, problemNumber={}, description={}", id,problemNumber,description);
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    labProblemValidator.validate(newLabProblem);
    log.trace("addLabProblem - student validated");

    LabProblem result = repository.save(newLabProblem);
    log.trace("addLabProblem - method finished: result={}", result);
    return result;
  }

  /**
   * Returns all the lab problems in the ro.ubb.repository in a Set
   *
   * @return a Set which stores all the lab problems
   */
  @Override
  public List<LabProblem> getAllLabProblems() {
    log.trace("getAllLabProblems - method entered");
    List<LabProblem> result = repository.findAll();
    log.trace("getAllLabProblems - method finished: result={}",result);
    return result;
  }

  /** Return all Lab problems sorted by the sort criteria. */
  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    log.trace("getAllLabProblemsSorted - method entered");
    Iterable<LabProblem> problems = sort.sort(repository.findAll());
    log.trace("getAllLabProblemsSorted - students sorted");
    List<LabProblem> result = StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
    log.trace("getAllLabProblemsSorted - method finished: result={}",result);
    return result;
  }

  /**
   * Get Optional containing lab problem with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find lab problem by
   * @return Optional containing the sought LabProblem or null otherwise
   */
  @Override
  public LabProblem getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    log.trace("getLabProblemById - method entered: id={}", id);

    LabProblem labProblem = repository.findById(id).orElse(null);
    log.debug("getLabProblemById - method exit: labProblem={}", labProblem);
    return labProblem;

  }

  /**
   * Deletes a lab problem from the ro.ubb.repository
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  @Override
  public void deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    log.trace("deleteLabProblem - method entered: id={}", id);
    repository.deleteById(id);
    log.trace("deleteLabProblem - method finished");
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
  @Override
  @Transactional
  public void updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);
    log.trace("updateLabProblem - method entered: newLabProblem={}", newLabProblem);

    labProblemValidator.validate(newLabProblem);
    log.trace("updateLabProblem - labProblem validated");

    repository
        .findById(newLabProblem.getId())
        .ifPresent(
            labProblem -> {
              labProblem.setDescription(newLabProblem.getDescription());
              labProblem.setProblemNumber(newLabProblem.getProblemNumber());
              log.debug("updateStudent - updated: labProblem={}", newLabProblem);
            });
    log.trace("updateLabProblem - method finished");
  }

  /**
   * Filters the elements of the ro.ubb.repository by a given problem number
   *
   * @param problemNumberToFilterBy the problem number to be filtered by
   * @return a {@code Set} - of entities filtered by the given problem number
   */
  @Override
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    if (problemNumberToFilterBy < 0) {
      throw new IllegalArgumentException("problem number negative!");
    }
    log.trace("filterByProblemNumber - method entered: problemNumber={}", problemNumberToFilterBy);
    Iterable<LabProblem> labProblems = repository.findAll();
    Set<LabProblem> filteredLabProblems = new HashSet<>();
    labProblems.forEach(filteredLabProblems::add);
    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    log.debug("filterByGroup - method finished: result={}", filteredLabProblems);
    return filteredLabProblems;
  }
}
