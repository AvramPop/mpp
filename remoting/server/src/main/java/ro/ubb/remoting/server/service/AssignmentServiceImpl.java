package ro.ubb.remoting.server.service;

import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.AssignmentService;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.common.service.sort.Sort;
import ro.ubb.remoting.server.repository.Repository;
import ro.ubb.remoting.server.repository.SortingRepository;
import ro.ubb.remoting.server.service.validators.Validator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AssignmentServiceImpl implements AssignmentService {
  private SortingRepository<Long, Assignment> repository;
  private LabProblemService labProblemService;
  private StudentService studentService;
  private Validator<Assignment> assignmentValidator;

  public AssignmentServiceImpl(
      SortingRepository<Long, Assignment> repository,
      LabProblemService labProblemService,
      StudentService studentService,
      Validator<Assignment> assignmentValidator) {
    this.repository = repository;
    this.studentService = studentService;
    this.labProblemService = labProblemService;
    this.assignmentValidator = assignmentValidator;
  }

  @Override
  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    assignmentValidator.validate(assignment);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      return repository.save(assignment);
    }
    return Optional.of(assignment);
  }

  @Override
  public Set<Assignment> getAllAssignments() {
    Iterable<Assignment> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    Iterable<Assignment> problems = repository.findAll(sort);
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Optional<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id);
  }

  @Override
  public Optional<Assignment> deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }

  @Override
  public Optional<Assignment> updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      assignmentValidator.validate(assignment);
      return repository.update(assignment);
    }
    return Optional.of(assignment);
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

    return studentService.getAllStudents().stream()
        .filter(
            student ->
                assignments.stream()
                    .anyMatch(assignment -> assignment.getStudentId().equals(student.getId())))
        .map(
            student ->
                new AbstractMap.SimpleEntry<>(
                    student.getId(),
                    (double)
                            assignments.stream()
                                .filter(
                                    assignment -> assignment.getStudentId().equals(student.getId()))
                                .map(Assignment::getGrade)
                                .reduce(0, Integer::sum)
                        / (double)
                            assignments.stream()
                                .filter(
                                    assignment -> assignment.getStudentId().equals(student.getId()))
                                .count()))
        .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()));
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

    return labProblemService.getAllLabProblems().stream()
        .map(
            labProblem ->
                new AbstractMap.SimpleEntry<>(
                    labProblem.getId(),
                    assignments.stream()
                        .filter(
                            assignment -> assignment.getLabProblemId().equals(labProblem.getId()))
                        .count()))
        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())));
  }

  @Override
  public Optional<Double> averageGrade() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
      return Optional.of((double) gradesSum / (double) assignments.size());
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    Map<Student, List<LabProblem>> result =
        studentService.getAllStudents().stream()
            .collect(
                Collectors.toMap(
                    student -> student, this::getAllLabProblemsForAStudent));

    return Optional.of(result);
  }

  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getStudentId().equals(student.getId()))
        .map(assignment -> labProblemService.getLabProblemById(assignment.getLabProblemId()).get())
        .collect(Collectors.toList());
  }
}
