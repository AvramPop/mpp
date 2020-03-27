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
  public Future<LabProblem> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return null;
  }

  @Override
  public Future<Set<LabProblem>> getAllLabProblems() {
    return null;
  }

  @Override
  public Future<List<LabProblem>> getAllLabProblemsSorted(Sort sort) {
    return null;
  }
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }
  @Override
  public Future<LabProblem> getLabProblemById(Long id) {
    return null;
  }

  @Override
  public Future<LabProblem> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return null;
  }

}
