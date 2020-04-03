package ro.ubb.remoting.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Set;

public class LabProblemServiceClient implements LabProblemService {
  @Autowired private LabProblemService labProblemService;

  @Override
  public LabProblem addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return labProblemService.addLabProblem(id, problemNumber, description);
  }

  @Override
  public Set<LabProblem> getAllLabProblems() {
    return labProblemService.getAllLabProblems();
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    return labProblemService.getAllLabProblemsSorted(sort);
  }

  @Override
  public LabProblem getLabProblemById(Long id) {
    return labProblemService.getLabProblemById(id);
  }

  @Override
  public LabProblem deleteLabProblem(Long id) {
    return labProblemService.deleteLabProblem(id);
  }

  @Override
  public LabProblem updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    return labProblemService.updateLabProblem(id, problemNumber, description);
  }

  @Override
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {

    return labProblemService.filterByProblemNumber(problemNumberToFilterBy);
  }
}
