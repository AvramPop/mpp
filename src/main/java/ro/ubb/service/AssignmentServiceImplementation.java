package ro.ubb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.RepositoryException;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.AssignmentRepository;
import ro.ubb.service.validators.Validator;
import ro.ubb.repository.sort.Sort;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentServiceImplementation implements AssignmentService {
  public static final Logger log = LoggerFactory.getLogger(AssignmentServiceImplementation.class);
  @Autowired private AssignmentRepository repository;
  @Autowired private LabProblemService labProblemService;
  @Autowired private StudentService studentService;
  @Autowired private Validator<Assignment> assignmentValidator;

  @Override
  public Assignment addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    assignmentValidator.validate(assignment);
    String errorMessage = "";
    if (studentService.getStudentById(studentID) == null)
      errorMessage += "Student id is not in the database ";
    if (labProblemService.getLabProblemById(labProblemID) == null)
      errorMessage += "Lab problem id is not in the database";
    if (errorMessage.length() > 0) throw new RepositoryException(errorMessage);

    return repository.save(assignment);
  }

  @Override
  public Set<Assignment> getAllAssignments() {
    Iterable<Assignment> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  /** Return all Assignments sorted by the sort criteria. */
  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    Iterable<Assignment> problems = sort.sort(repository.findAll());
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
  }

  /**
   * Get Optional containing assignment with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find assignment by
   * @return Optional containing the sought Assignment or null otherwise
   */
  @Override
  public Assignment getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findById(id).orElse(null);
  }

  /**
   * Deletes an assignment from the ro.ubb.repository
   *
   * @param id the id of the assignment to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  @Override
  public void deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    repository.deleteById(id);
  }

  /**
   * Updates an assignment inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  @Override
  public void updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);
    assignmentValidator.validate(newAssignment);
    log.trace("updateAssignment - method entered: assignment={}", newAssignment);
    if (studentService.getStudentById(studentID) != null
        && labProblemService.getLabProblemById(labProblemID) != null) {

      repository
          .findById(newAssignment.getId())
          .ifPresent(
              assignment -> {
                assignment.setGrade(newAssignment.getGrade());
                assignment.setStudentId(newAssignment.getStudentId());
                assignment.setLabProblemId(newAssignment.getLabProblemId());
              });
    }
  }

  /**
   * Returns the student id who has the biggest mean of grades
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade
   *     average
   */
  @Override
  public AbstractMap.SimpleEntry<Long, Double> greatestMean() {
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
        .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()))
        .orElse(null);
  }

  /**
   * Returns the id of the lab problem which was assigned the most often
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of
   *     assignments
   */
  @Override
  public AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned() {
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
        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())))
        .orElse(null);
  }

  /**
   * Returns the average grade of all the groups
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise a
   *     {@code Double} which represents the average grade
   */
  @Override
  public Double averageGrade() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
      return (double) gradesSum / (double) assignments.size();
    } else {
      return null;
    }
  }

  /**
   * Return a mapping of every Student and a list of it's assigned LabProblems.
   *
   * @return the sought Student - List of LabProblems. If student has no assignment, map to an empty
   *     list.
   */
  @Override
  public Map<Student, List<LabProblem>> studentAssignedProblems() {

    return studentService.getAllStudents().stream()
        .collect(Collectors.toMap(student -> student, this::getAllLabProblemsForAStudent));
  }

  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    return repository.findAll().stream()
        .filter(assignment -> assignment.getStudentId().equals(student.getId()))
        .map(assignment -> labProblemService.getLabProblemById(assignment.getLabProblemId()))
        .collect(Collectors.toList());
  }
}
