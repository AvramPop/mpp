package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.sort.Sort;
import ro.ubb.socket.server.repository.SortingRepository;
import ro.ubb.socket.server.service.validators.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabProblemServerService implements LabProblemService {
  private SortingRepository<Long, LabProblem> repository;
  private Validator<LabProblem> validator;
  private ExecutorService executorService;

  public LabProblemServerService(SortingRepository<Long, LabProblem> repository, Validator<LabProblem> validator, ExecutorService executorService){
    this.repository = repository;
    this.validator = validator;
    this.executorService = executorService;
  }

  @Override
  public Future<Boolean> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    validator.validate(newLabProblem);

    return executorService.submit(() -> repository.save(newLabProblem).isEmpty());
  }

  @Override
  public Future<Boolean> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem labProblem = new LabProblem(problemNumber, description);
    labProblem.setId(id);
    validator.validate(labProblem);
    return executorService.submit(() -> repository.update(labProblem).isEmpty());

  }
  @Override
  public Future<Set<LabProblem>> getAllLabProblems() {
    Iterable<LabProblem> labProblems = repository.findAll();
    return executorService.submit(
        () -> StreamSupport.stream(labProblems.spliterator(), false).collect(Collectors.toSet()));

  }

  @Override
  public Future<List<LabProblem>> getAllLabProblemsSorted(Sort sort) {
    Iterable<LabProblem> labProblems = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(labProblems.spliterator(), false).collect(Collectors.toList()));

  }

  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }
  @Override
  public Future<LabProblem> getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());

  }



}
