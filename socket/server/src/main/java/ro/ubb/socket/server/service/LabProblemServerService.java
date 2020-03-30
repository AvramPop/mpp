package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.sort.Sort;
import ro.ubb.socket.server.repository.SortingRepository;
import ro.ubb.socket.server.service.validators.Validator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
/** Server service to handle Lab Problem specific data communicated via socket. */
public class LabProblemServerService implements LabProblemService {
  private SortingRepository<Long, LabProblem> repository;
  private Validator<LabProblem> validator;
  private ExecutorService executorService;

  public LabProblemServerService(
      SortingRepository<Long, LabProblem> repository,
      Validator<LabProblem> validator,
      ExecutorService executorService) {
    this.repository = repository;
    this.validator = validator;
    this.executorService = executorService;
  }
  /**
   * Performs the saving of the new entity to the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    validator.validate(newLabProblem);

    return executorService.submit(() -> repository.save(newLabProblem).isEmpty());
  }
  /**
   * Performs the update of a lab problem entity in the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem labProblem = new LabProblem(problemNumber, description);
    labProblem.setId(id);
    validator.validate(labProblem);
    return executorService.submit(() -> repository.update(labProblem).isEmpty());
  }
  /**
   * Returns all lab problems from the database
   *
   * @return a future containing a set of elements
   */
  @Override
  public Future<Set<LabProblem>> getAllLabProblems() {
    Iterable<LabProblem> labProblems = repository.findAll();
    return executorService.submit(
        () -> StreamSupport.stream(labProblems.spliterator(), false).collect(Collectors.toSet()));
  }
  /**
   * Returns all lab problems from the database sorted by the sort entity
   *
   * @return a future containing a set of elements sorted
   */
  @Override
  public Future<List<LabProblem>> getAllLabProblemsSorted(Sort sort) {
    Iterable<LabProblem> labProblems = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(labProblems.spliterator(), false).collect(Collectors.toList()));
  }
  /**
   * Deletes the lab problem with the id from the database
   *
   * @param id the id of the student to be deleted
   * @return a future containing true if the operation is successfull otherwise false
   */
  @Override
  public Future<Boolean> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return executorService.submit(() -> repository.delete(id).isPresent());
  }
  /**
   * Returns a lab problem given by the id
   *
   * @param id to find student by
   * @return a Future containing the entity, or null of the entity doesn't exist
   */
  @Override
  public Future<LabProblem> getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());
  }
}
