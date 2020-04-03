package ro.ubb.remoting.server.service;

import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.sort.Sort;
import ro.ubb.remoting.server.repository.SortingRepository;
import ro.ubb.remoting.server.service.validators.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabProblemServiceImpl implements LabProblemService {
  private SortingRepository<Long, LabProblem> repository;
  private Validator<LabProblem> labProblemValidator;

  public LabProblemServiceImpl(
      SortingRepository<Long, LabProblem> repository, Validator<LabProblem> labProblemValidator) {
    this.repository = repository;
    this.labProblemValidator = labProblemValidator;
  }

  @Override
  public LabProblem addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    labProblemValidator.validate(newLabProblem);

    return repository.save(newLabProblem).get();
  }

  @Override
  public Set<LabProblem> getAllLabProblems() {
    Iterable<LabProblem> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    Iterable<LabProblem> problems = repository.findAll(sort);
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public LabProblem getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id).get();
  }

  @Override
  public LabProblem deleteLabProblem(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id).get();
  }

  @Override
  public LabProblem updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    labProblemValidator.validate(newLabProblem);

    return repository.update(newLabProblem).get();
  }

  @Override
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
