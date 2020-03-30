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
/** Server service to handle Assignment specific data communicated via socket. */
public class AssignmentServerService implements AssignmentService {
  private SortingRepository<Long, Assignment> repository;
  private Validator<Assignment> validator;
  private ExecutorService executorService;
  private LabProblemService labProblemService;
  private StudentService studentService;

  public AssignmentServerService(
      SortingRepository<Long, Assignment> repository,
      Validator<Assignment> validator,
      ExecutorService executorService,
      LabProblemService labProblemService,
      StudentService studentService) {
    this.repository = repository;
    this.validator = validator;
    this.executorService = executorService;
    this.labProblemService = labProblemService;
    this.studentService = studentService;
  }
  /**
   * Performs the saving of the new entity to the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);

    validator.validate(newAssignment);

    return executorService.submit(() -> repository.save(newAssignment).isEmpty());
  }
  /**
   * Performs the update of an assignment entity in the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    validator.validate(assignment);
    return executorService.submit(() -> repository.update(assignment).isEmpty());
  }
  /**
   * Deletes the assignment with the id from the database
   *
   * @param id the id of the student to be deleted
   * @return a future containing true if the operation is successfull otherwise false
   */
  @Override
  public Future<Boolean> deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return executorService.submit(() -> repository.delete(id).isPresent());
  }
  /**
   * Returns all assignments from the database
   *
   * @return a future containing a set of elements
   */
  @Override
  public Future<Set<Assignment>> getAllAssignments() {
    Iterable<Assignment> problems = repository.findAll();
    return executorService.submit(
        () -> StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet()));
  }
  /**
   * Returns all lab problems from the database sorted by the sort entity
   *
   * @return a future containing a set of elements sorted
   */
  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort) {
    Iterable<Assignment> assignments = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(assignments.spliterator(), false).collect(Collectors.toList()));
  }
  /**
   * Returns an assignment given by the id
   *
   * @param id to find student by
   * @return a Future containing the entity, or null of the entity doesn't exist
   */
  @Override
  public Future<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());
  }
  /**
   * Returns the student id who has the biggest mean of grades from the database
   *
   * @return a {@code Future} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade
   *     average
   */
  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    try {
      AbstractMap.SimpleEntry<Long, Double> result =
          studentService.getAllStudents().get().stream()
              .filter(
                  student ->
                      assignments.stream()
                          .anyMatch(
                              assignment -> assignment.getStudentId().equals(student.getId())))
              .map(
                  student ->
                      new AbstractMap.SimpleEntry<>(
                          student.getId(),
                          (double)
                                  assignments.stream()
                                      .filter(
                                          assignment ->
                                              assignment.getStudentId().equals(student.getId()))
                                      .map(Assignment::getGrade)
                                      .reduce(0, Integer::sum)
                              / (double)
                                  assignments.stream()
                                      .filter(
                                          assignment ->
                                              assignment.getStudentId().equals(student.getId()))
                                      .count()))
              .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()))
              .get();
      return executorService.submit(() -> result);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
  /**
   * Returns the id of the lab problem which was assigned the most often from the database
   *
   * @return a {@code Future} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of
   *     assignments
   */
  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

    try {
      AbstractMap.SimpleEntry<Long, Long> result =
          labProblemService.getAllLabProblems().get().stream()
              .map(
                  labProblem ->
                      new AbstractMap.SimpleEntry<>(
                          labProblem.getId(),
                          assignments.stream()
                              .filter(
                                  assignment ->
                                      assignment.getLabProblemId().equals(labProblem.getId()))
                              .count()))
              .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())))
              .get();
      return executorService.submit(() -> result);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
  /**
   * Returns the average grade of all the groups from the database
   *
   * @return a {@code Future} containing a null if no student is in the repository otherwise a
   *     {@code Double} which represents the average grade
   */
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
  /**
   * Return a mapping of every Student and a list of it's assigned LabProblems from the database
   *
   * @return a {@code Future} containing the sought Student - List of LabProblems. If student has no
   *     assignment, map to an empty list.
   */
  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    try {
      Map<Student, List<LabProblem>> result =
          studentService.getAllStudents().get().stream()
              .collect(Collectors.toMap(student -> student, this::getAllLabProblemsForAStudent));
      return executorService.submit(() -> result);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns all the lab problem assigned to a student
   *
   * @param student the student for which to find the lab problems
   * @return a {@code List} containing the lab problem entities
   */
  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .filter(assignment -> assignment.getStudentId().equals(student.getId()))
        .map(
            assignment -> {
              try {
                return labProblemService.getLabProblemById(assignment.getLabProblemId()).get();
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
              return null;
            })
        .collect(Collectors.toList());
  }
}
