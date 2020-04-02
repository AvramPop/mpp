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
  @Autowired
  private AssignmentService assignmentService;
  @Override
  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
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
  public Optional<Assignment> getAssignmentById(Long id) {
    return assignmentService.getAssignmentById(id);
  }

  @Override
  public Optional<Assignment> deleteAssignment(Long id) {
    return assignmentService.deleteAssignment(id);
  }

  @Override
  public Optional<Assignment> updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    return assignmentService.updateAssignment(id, studentID, labProblemID, grade);
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    return assignmentService.greatestMean();
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    return idOfLabProblemMostAssigned();
  }

  @Override
  public Optional<Double> averageGrade() {
    return averageGrade();
  }

  @Override
  public Optional<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    return studentAssignedProblems();
  }
}
