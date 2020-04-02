package ro.ubb.remoting.client.service;

import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LabProblemServiceClient implements LabProblemService {
  @Override
  public Optional<LabProblem> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Set<LabProblem> getAllLabProblems() {
    return null;
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    return null;
  }

  @Override
  public Optional<LabProblem> getLabProblemById(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<LabProblem> deleteLabProblem(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<LabProblem> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    return null;
  }
}
