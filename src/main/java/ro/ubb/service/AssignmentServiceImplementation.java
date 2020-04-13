package ro.ubb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private static final Logger log = LoggerFactory.getLogger(AssignmentServiceImplementation.class);
  @Autowired private AssignmentRepository repository;
  @Autowired private LabProblemService labProblemService;
  @Autowired private StudentService studentService;
  @Autowired private Validator<Assignment> assignmentValidator;

  @Override
  public Assignment addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    log.trace("addLabProblem - method entered: id={}, studentID={}, labProblemID={}. grade={}", id,studentID,labProblemID,grade);

    String errorMessage = "";
    Student student = studentService.getStudentById(studentID);
    if (student == null)
      errorMessage += "Student id is not in the database ";
    LabProblem labProblem = labProblemService.getLabProblemById(labProblemID);
    if (labProblem == null)
          errorMessage += "Lab problem id is not in the database";
    if (errorMessage.length() > 0) throw new RepositoryException(errorMessage);
    Assignment assignment = new Assignment(student, labProblem, grade);
    assignment.setId(id);
    assignmentValidator.validate(assignment);
    log.trace("addAssignment - assignment validated");
    
    Assignment result = repository.save(assignment);
      log.trace("addAssignment - method finished: result={}", result);
    return result;
  }

  @Override
  public List<Assignment> getAllAssignments() {
      log.trace("getAllAssignments - method entered");
      List<Assignment> result = repository.findAll();
      log.trace("getAllAssignments - method finished: result={}",result);
      return result;
  }

  /** Return all Assignments sorted by the sort criteria. */
  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
      log.trace("getAllAssignmentsSorted - method entered");
      Iterable<Assignment> assignments = sort.sort(repository.findAll());
      log.trace("getAllAssignmentsSorted - students sorted");
      List<Assignment> result = StreamSupport.stream(assignments.spliterator(), false).collect(Collectors.toList());
      log.trace("getAllAssignmentsSorted - method finished: result={}",result);
      return result;
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
      log.trace("getAssignmentById - method entered: id={}", id);

      Assignment assignment = repository.findById(id).orElse(null);
      log.debug("getAssignmentById - method exit: labProblem={}", assignment);
      return assignment;
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
      log.trace("deleteAssignment - method entered: id={}", id);
      repository.deleteById(id);
      log.trace("deleteAssignment - method finished");
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
  @Transactional
  public void updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    log.trace("updateAssignment - method entered: studentId {}, labProblemID {}, grade{}", studentID, labProblemID,grade);

    String errorMessage = "";
    Student student = studentService.getStudentById(studentID);
    if (student == null)
      errorMessage += "Student id is not in the database ";
    LabProblem labProblem = labProblemService.getLabProblemById(labProblemID);
    if (labProblem == null)
      errorMessage += "Lab problem id is not in the database";
    if (errorMessage.length() > 0) throw new RepositoryException(errorMessage);
    Assignment newAssignment = new Assignment(student, labProblem, grade);
    newAssignment.setId(id);


    repository
          .findById(newAssignment.getId())
          .ifPresent(
              assignmentObj -> {
                assignmentObj.setGrade(newAssignment.getGrade());
                assignmentObj.setStudent(newAssignment.getStudent());
                assignmentObj.setLabProblem(newAssignment.getLabProblem());
                log.debug("updateAssignment - updated: assignmentObj={}", assignmentObj);
              });
      log.trace("updateAssignment - method finished");
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
    log.trace("greatestMean - method entered");
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

    AbstractMap.SimpleEntry<Long,Double> result = studentService.getAllStudents().stream()
        .filter(
            student ->
                assignments.stream()
                    .anyMatch(assignment -> assignment.getStudent().getId().equals(student.getId())))
        .map(
            student ->
                new AbstractMap.SimpleEntry<>(
                    student.getId(),
                    (double)
                            assignments.stream()
                                .filter(
                                    assignment -> assignment.getLabProblem().getId().equals(student.getId()))
                                .map(Assignment::getGrade)
                                .reduce(0, Integer::sum)
                        / (double)
                            assignments.stream()
                                .filter(
                                    assignment -> assignment.getStudent().getId().equals(student.getId()))
                                .count()))
        .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()))
        .orElse(null);
      log.trace("greatestMean - method finished: result={}",result);
      return result;
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
      log.trace("idOfLabProblemMostAssigned - method entered");
      Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());

      AbstractMap.SimpleEntry<Long, Long> result =  labProblemService.getAllLabProblems().stream()
        .map(
            labProblem ->
                new AbstractMap.SimpleEntry<>(
                    labProblem.getId(),
                    assignments.stream()
                        .filter(
                            assignment -> assignment.getLabProblem().getId().equals(labProblem.getId()))
                        .count()))
        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())))
        .orElse(null);
      log.trace("idOfLabProblemMostAssigned - method finished: result={}",result);
      return result;

  }

  /**
   * Returns the average grade of all the groups
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise a
   *     {@code Double} which represents the average grade
   */
  @Override
  public Double averageGrade() {
      log.trace("averageGrade - method entered");
      Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
        Double result = (double) gradesSum / (double) assignments.size();
        log.trace("averageGrade - method finished: result={}",result);
        return result;
    } else {
        log.trace("averageGrade - method finished: result={null value}");
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
  @Transactional
  public Map<Student, List<LabProblem>> studentAssignedProblems() {
      log.trace("studentAssignedProblems - method entered");
    Map<Student,List<LabProblem>> result = studentService.getAllStudents().stream()
        .collect(Collectors.toMap(student -> student, this::getAllLabProblemsForAStudent));
      log.trace("studentAssignedProblems - method finished: result={}",result);
      return result;
  }

  @Transactional
  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    System.out.println("Hello");
      return new ArrayList<>(student.getLabProblems());
//      return repository.findAll().stream()
//        .filter(assignment -> assignment.getStudent().getId().equals(student.getId()))
//        .map(assignment -> labProblemService.getLabProblemById(assignment.getLabProblem().getId()))
//        .collect(Collectors.toList());
  }
}
