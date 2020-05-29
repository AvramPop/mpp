package ro.ubb.catalog.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;

@Service
public interface LabProblemService {
  List<LabProblem> getAllLabProblems();

  Page<LabProblem> getAllLabProblems(int pageNumber, int perPage);

  List<LabProblem> getAllLabProblemsSorted(Sort sort);

  boolean saveLabProblem(LabProblem labProblem);

  boolean updateLabProblem(Long id, LabProblem labProblem);

  boolean deleteLabProblem(Long id);

  LabProblem getLabProblem(Long id);
  AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned();

  List<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy);
  boolean saveAssignment(Long id, Long studentId, Long labProblemId, int grade);
  List<LabProblem> findByProblemNumberCustom(int problemNumber);

  List<LabProblem> findByDescriptionCustom(String description);
//  List<LabProblem> findByProblemNumber(int problemNumber);
}
