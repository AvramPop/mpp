package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.Assignment;
import ro.ubb.springjpa.model.LabProblem;
import ro.ubb.springjpa.model.Student;
import ro.ubb.springjpa.repository.AssignmentRepository;
import ro.ubb.springjpa.service.sort.Sort;
import ro.ubb.springjpa.service.validator.AssignmentValidator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentServiceImpl implements AssignmentService {
  public static final Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);
  @Autowired private AssignmentRepository repository;
  @Autowired private AssignmentValidator validator;
  @Autowired private LabProblemService labProblemService;
  @Autowired private StudentService studentService;

  @Override
  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {

    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    log.trace("addAssignment - method entered: assignment={}", assignment);
    assignment.setId(id);
    validator.validate(assignment);
    if(getAssignmentById(id).isPresent()) return Optional.of(assignment);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      try {
        log.debug("addAssignment - updated: a={}", assignment);

        repository.save(assignment);
        log.trace("addAssignment - finished well");
        return Optional.empty();
      } catch (JpaSystemException e) {
        log.trace("addAssignment - finished bad");
        return Optional.of(assignment);
      }
    }
    log.trace("addAssignment - finished bad");
    return Optional.of(assignment);
  }

  @Override
  public Set<Assignment> getAllAssignments() {
    log.trace("getAllAssignments - method entered");
    Iterable<Assignment> problems = repository.findAll();
    log.trace("getAllAssignments - finished well");
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    log.trace("getAllAssignmentsSorted - method entered");
    Iterable<Assignment> assignments = repository.findAll();
    Iterable<Assignment> assignmentsSorted = sort.sort(assignments);
    log.trace("getAllAssignmentsSorted - finished well");
    return StreamSupport.stream(assignmentsSorted.spliterator(), false)
        .collect(Collectors.toList());
  }

  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    log.trace("deleteStudent - method entered: student id={}", id);
    if (studentService.getStudentById(id).isEmpty()) return Optional.empty();

    Set<Assignment> allAssignments = this.getAllAssignments();
    log.debug("deleteStudent - deleted: a={}", id);
    allAssignments.stream()
        .filter(entity -> entity.getStudentId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));
Optional<Student> result = studentService.deleteStudent(id);
    if (result.isPresent()) {
      log.trace("deleteStudent - finished well");
    } else {
      log.trace("deleteStudent - finished bad");
    }
    return result;
  }
  /**
   * Deletes a lab problem from the ro.ubb.repository and also deletes all assignments corresponding
   * to that student
   *
   * @param id the id of the lab problem to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    log.trace("deleteLabProblem - method entered: delete={}", id);
    if (labProblemService.getLabProblemById(id).isEmpty()) return Optional.empty();
    log.debug("deleteLabProblem - deleted: a={}", id);
    Set<Assignment> allAssignments = this.getAllAssignments();

    allAssignments.stream()
        .filter(entity -> entity.getLabProblemId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));

    Optional<LabProblem> result =  labProblemService.deleteLabProblem(id);
    if (result.isPresent()) {
      log.trace("deleteLabProblem - finished well");
    } else {
      log.trace("deleteLabProblem - finished bad");
    }
    return result;
  }

  @Override
  public Optional<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    log.trace("getAssignmentById - method entered");
    return repository.findById(id);
  }

  @Override
  public Optional<Assignment> deleteAssignment(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      log.trace("deleteAssignment - method entered: assignment={}", id);
      log.debug("addAssignment - delete: a={}", id);
      Optional<Assignment> entity = repository.findById(id);
      repository.deleteById(id);
      log.trace("addAssignment - finished well");
      return entity;
    } catch (EmptyResultDataAccessException e) {
      log.trace("addAssignment - finished bad");
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public Optional<Assignment> updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    validator.validate(assignment);
    if(getAssignmentById(id).isEmpty()) return Optional.of(assignment);

    log.trace("updateAssignment - method entered: assignment={}", assignment);
    try {
      repository
          .findById(assignment.getId())
          .ifPresent(
              a -> {
                a.setStudentId(assignment.getStudentId());
                a.setLabProblemId(assignment.getLabProblemId());
                a.setGrade(assignment.getGrade());
                a.setId(assignment.getId());
                log.debug("updateAssignment - updated: a={}", a);
              });
      log.trace("updateAssignment - method finished");
      return Optional.empty();
    } catch (JpaSystemException e) {
      return Optional.of(assignment);
    }
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    log.trace("greatestMean - method entered");
    return studentService.getAllStudents().stream()
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
        .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()));
  }

  @Override
  public Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    log.trace("idOfLabProblemMostAssigned - method entered");
    return labProblemService.getAllLabProblems().stream()
        .map(
            labProblem ->
                new AbstractMap.SimpleEntry<Long, Long>(
                    labProblem.getId(),
                    assignments.stream()
                        .filter(
                            assignment -> assignment.getLabProblemId().equals(labProblem.getId()))
                        .count()))
        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())));
  }

  @Override
  public Optional<Double> averageGrade() {
    log.trace("averageGrade - method entered");
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
    log.trace("studentAssignedProblems - method entered");

    Map<Student, List<LabProblem>> result =
        studentService.getAllStudents().stream()
            .collect(Collectors.toMap(student -> student, this::getAllLabProblemsForAStudent));
    return Optional.of(result);
  }

  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
    return repository.findAll().stream()
        .filter(assignment -> assignment.getStudentId().equals(student.getId()))
        .map(assignment -> labProblemService.getLabProblemById(assignment.getLabProblemId()).get())
        .collect(Collectors.toList());
  }
}
