package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;
import ro.ubb.socket.server.repository.SortingRepository;
import ro.ubb.socket.server.service.validators.Validator;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AssignmentServerService implements AssignmentService {
  private SortingRepository<Long, Assignment> repository;
  private Validator<Assignment> validator;
  private ExecutorService executorService;
  private LabProblemService labProblemService;
  private StudentService studentService;

  public AssignmentServerService(SortingRepository<Long, Assignment> repository, Validator<Assignment> validator, ExecutorService executorService, LabProblemService labProblemService, StudentService studentService){
    this.repository = repository;
    this.validator = validator;
    this.executorService = executorService;
    this.labProblemService = labProblemService;
    this.studentService = studentService;
  }

  @Override
  public Future<Boolean> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);

    validator.validate(newAssignment);

    return executorService.submit(() -> repository.save(newAssignment).isEmpty());

  }

  @Override
  public Future<Boolean> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    validator.validate(assignment);
    return executorService.submit(() -> repository.update(assignment).isEmpty());

  }


  @Override
  public Future<Boolean> deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return executorService.submit(() -> repository.delete(id).isPresent());
  }

  @Override
  public Future<Boolean> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");

    try{
      if (studentService.getStudentById(id).get() == null) return executorService.submit(() -> null);
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting student");
    }
    Set<Assignment> allAssignments = null;
    try{
      allAssignments = this.getAllAssignments().get();
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting assignments");
    }
    allAssignments.stream()
        .filter(entity -> entity.getStudentId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));
    return executorService.submit(() -> studentService.deleteStudent(id).isPresent());
  }

  @Override
  public Future<Boolean> deleteLabProblem(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");

    try{
      if (labProblemService.getLabProblemById(id).get() == null) return executorService.submit(() -> null);
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting lab problem");
    }
    Set<Assignment> allAssignments = null;
    try{
      allAssignments = this.getAllAssignments().get();
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting assignments");
    }
    System.out.println("here2");
    allAssignments.stream()
        .filter(entity -> entity.getLabProblemId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));
    return executorService.submit(() -> labProblemService.deleteLabProblem(id).isPresent());
  }


  @Override
  public Future<Set<Assignment>> getAllAssignments()    {
    Iterable<Assignment> problems = repository.findAll();
    return executorService.submit(() -> StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet()));
  }

  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort) {
    Iterable<Assignment> assignments = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(assignments.spliterator(), false).collect(Collectors.toList()));

  }

  @Override
  public Future<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());

  }



  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    try{
      AbstractMap.SimpleEntry<Long, Double> result = studentService.getAllStudents().get()
          .stream()
          .filter(
              student ->
                  assignments.stream()
                      .anyMatch(assignment -> assignment.getStudentId().equals(student.getId())))
          .map(
              student ->
                  new AbstractMap.SimpleEntry<Long, Double>(
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
          .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())).get();
      return executorService.submit(() -> result);
    } catch(InterruptedException | ExecutionException e){
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

    try{
      AbstractMap.SimpleEntry<Long, Long> result = labProblemService.getAllLabProblems().get()
          .stream()
          .map(
              labProblem ->
                  new AbstractMap.SimpleEntry<Long, Long>(
                      labProblem.getId(),
                      assignments.stream()
                          .filter(
                              assignment -> assignment.getLabProblemId().equals(labProblem.getId()))
                          .count()))
          .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()))).get();
      return executorService.submit(() -> result);
    } catch(InterruptedException | ExecutionException e){
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Future<Double> averageGrade() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
      return executorService.submit(() -> (double) gradesSum / (double) assignments.size());
    } else {
      return null;
    }
  }

  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    try{
      Map<Student, List<LabProblem>> result =
          studentService.getAllStudents().get().stream()
              .collect(
                  Collectors.toMap(
                      student -> student, this::getAllLabProblemsForAStudent));
      return executorService.submit(() -> result);
    } catch(InterruptedException | ExecutionException e){
      e.printStackTrace();
    }
    return null;
  }

  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
    .filter(assignment -> assignment.getStudentId().equals(student.getId()))
    .map(assignment -> {
      try{
        return labProblemService.getLabProblemById(assignment.getLabProblemId()).get();
      } catch(InterruptedException | ExecutionException e){
        e.printStackTrace();
      }
      return null;
    })
    .collect(Collectors.toList());
  }
}
