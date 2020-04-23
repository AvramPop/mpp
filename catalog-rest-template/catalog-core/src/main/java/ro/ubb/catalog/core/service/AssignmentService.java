package ro.ubb.catalog.core.service;

import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;

@Service
public interface AssignmentService {
  List<Assignment> getAllAssignments();

  List<Assignment> getAllAssignmentsSorted(Sort sort);

  boolean saveAssignment(Assignment assignment);

  boolean updateAssignment(Long id, Assignment assignment);

  boolean deleteAssignment(Long id);

  Assignment getAssignmentById(Long id);

  AbstractMap.SimpleEntry<Long, Double> greatestMean();

  AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned();

  Double averageGrade();

  boolean deleteStudent(Long id);

  boolean deleteLabProblem(Long id);
}
