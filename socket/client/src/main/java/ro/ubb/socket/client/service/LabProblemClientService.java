package ro.ubb.socket.client.service;

import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public class LabProblemClientService implements LabProblemService {
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

  @Override
  public Future<LabProblem> getLabProblemById(Long id) {
    return null;
  }

  @Override
  public Future<LabProblem> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return null;
  }

  @Override
  public Optional<LabProblem> deleteLabProblem(Long id){ // this is not needed here but necessary for interface contract
    return Optional.empty();
  }
}
