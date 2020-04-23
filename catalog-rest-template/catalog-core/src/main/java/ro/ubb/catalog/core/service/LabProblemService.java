package ro.ubb.catalog.core.service;

import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.service.sort.Sort;

import java.util.List;

@Service
public interface LabProblemService {
  List<LabProblem> getAllLabProblems();

  List<LabProblem> getAllLabProblemsSorted(Sort sort);

  boolean saveLabProblem(LabProblem labProblem);

  boolean updateLabProblem(Long id, LabProblem labProblem);

  boolean deleteLabProblem(Long id);

  LabProblem getLabProblem(Long id);

  List<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy);
}
