package service;

import domain.LabProblem;
import domain.exceptions.ValidatorException;
import repository.Repository;

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
  public void addLabProblem(LabProblem labProblem) throws ValidatorException {
    repository.save(labProblem);
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
}
