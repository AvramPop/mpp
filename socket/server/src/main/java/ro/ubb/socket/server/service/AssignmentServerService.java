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
  public Future<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    return null;
  }

  @Override
  public Future<Set<Assignment>> getAllAssignments()    {
    Iterable<Assignment> problems = repository.findAll();
    return executorService.submit(() -> StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet()));
  }

  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort) {
    return null;
  }

  @Override
  public Future<Assignment> getAssignmentById(Long id) {
    return null;
  }

  @Override
  public Future<Assignment> deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return executorService.submit(() -> repository.delete(id).get());
  }

  @Override
  public Future<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");

    try{
      if (studentService.getStudentById(id).get() == null) return executorService.submit(() -> null);
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting student");
    }
    System.out.println("here1");
    Set<Assignment> allAssignments = null;
    try{
      allAssignments = this.getAllAssignments().get();
    } catch(InterruptedException | ExecutionException e){
      System.err.println("exception while getting assignments");
    }
    System.out.println("here2");
    allAssignments.stream()
        .filter(entity -> entity.getStudentId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));
    System.out.println("here3");
    return executorService.submit(() -> studentService.deleteStudent(id).get());
  }

  @Override
  public Future<LabProblem> deleteLabProblem(Long id) {
    return null;
  }

  @Override
  public Future<Assignment> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    return null;
  }

  @Override
  public Future<Double> averageGrade() {
    return null;
  }

  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    return null;
  }

}
