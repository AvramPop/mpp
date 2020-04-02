package ro.ubb.remoting.client.service;

import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.AssignmentService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.*;

public class AssignmentServiceClient implements AssignmentService {
  @Override
  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Set<Assignment> getAllAssignments() {
    return null;
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    return null;
  }

  @Override
  public Optional<Assignment> getAssignmentById(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<Assignment> deleteAssignment(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<Student> deleteStudent(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<LabProblem> deleteLabProblem(Long id) {
    return Optional.empty();
  }

  @Override
  public Optional<Assignment> updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    return Optional.empty();
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    return Optional.empty();
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> averageGrade() {
    return Optional.empty();
  }

  @Override
  public Optional<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    return Optional.empty();
  }
}
