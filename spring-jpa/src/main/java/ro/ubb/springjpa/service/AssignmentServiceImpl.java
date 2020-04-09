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
    assignment.setId(id);
    validator.validate(assignment);
    if(getAssignmentById(id).isPresent()) return Optional.of(assignment);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      try {
        repository.save(assignment);
        return Optional.empty();
      } catch (JpaSystemException e) {
        return Optional.of(assignment);
      }
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
    Iterable<Assignment> assignments = repository.findAll();
    Iterable<Assignment> assignmentsSorted = sort.sort(assignments);
    return StreamSupport.stream(assignmentsSorted.spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findById(id);
  }

  @Override
  public Optional<Assignment> deleteAssignment(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      Optional<Assignment> entity = repository.findById(id);
      repository.deleteById(id);
      return entity;
    } catch (EmptyResultDataAccessException e) {
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

  public Optional<Map<Student, List<LabProblem>>> studentAssignedProblems() {

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
