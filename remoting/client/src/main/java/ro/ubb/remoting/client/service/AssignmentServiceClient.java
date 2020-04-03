package ro.ubb.remoting.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.AssignmentService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.*;

public class AssignmentServiceClient implements AssignmentService {
  @Autowired private AssignmentService assignmentService;

  @Override
  public Assignment addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    return assignmentService.addAssignment(id, studentID, labProblemID, grade);
  }

  @Override
  public Set<Assignment> getAllAssignments() {
    return assignmentService.getAllAssignments();
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    return assignmentService.getAllAssignmentsSorted(sort);
  }

  @Override
  public Assignment getAssignmentById(Long id) {
    return assignmentService.getAssignmentById(id);
  }

  @Override
  public Assignment deleteAssignment(Long id) {
    return assignmentService.deleteAssignment(id);
  }

  @Override
  public Assignment updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    return assignmentService.updateAssignment(id, studentID, labProblemID, grade);
  }

  @Override
  public AbstractMap.SimpleEntry<Long, Double> greatestMean() {
    return assignmentService.greatestMean();
  }

  @Override
  public AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned() {
    return assignmentService.idOfLabProblemMostAssigned();
  }

  @Override
  public Double averageGrade() {
    return assignmentService.averageGrade();
  }

  @Override
  public Map<Student, List<LabProblem>> studentAssignedProblems() {
    return assignmentService.studentAssignedProblems();
  }
}
