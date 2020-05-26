package ro.ubb.catalog.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;

@Service
public interface AssignmentService {
  List<Assignment> getAllAssignments();

  Page<Assignment> getAllAssignments(int pageNumber, int perPage);

  List<Assignment> getAllAssignmentsSorted(Sort sort);

  boolean saveAssignment(Long id, Long studentId, Long labProblemId, int grade);

  boolean updateAssignment(Long id, Long studentId, Long labProblemId, int grade);

  boolean deleteAssignment(Long id);

  Assignment getAssignmentById(Long id);

  AbstractMap.SimpleEntry<Long, Double> greatestMean();

  AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned();

  Double averageGrade();

  boolean deleteStudent(Long id);

  boolean deleteLabProblem(Long id);
}
