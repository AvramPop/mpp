package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.repository.AssignmentRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.AssignmentValidator;

import javax.persistence.EntityNotFoundException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AssignmentServiceImpl implements AssignmentService {
  public static final Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);

  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private StudentService studentService;
  @Autowired private LabProblemService labProblemService;
  @Autowired private AssignmentValidator validator;

  @Override
  public List<Assignment> getAllAssignments() {
    log.trace("getAllAssignments --- method entered");

    List<Assignment> result = assignmentRepository.findAll();

    log.trace("getAllAssignments: result={}", result);

    return result;
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    log.trace("getAllAssignmentsSorted - method entered");
    Iterable<Assignment> assignments = assignmentRepository.findAll();
    Iterable<Assignment> assignmentsSorted = sort.sort(assignments);
    log.trace("getAllAssignmentsSorted - finished well");
    return StreamSupport.stream(assignmentsSorted.spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public boolean saveAssignment(Assignment assignment) {
    log.trace("saveAssignment - finished well");
    validator.validate(assignment);
    if (assignmentRepository.existsById(assignment.getId())) return false;
    assignmentRepository.save(assignment);
    return true;
  }

  @Override
  @Transactional
  public boolean updateAssignment(Long id, Assignment assignment) {
    // todo log
    log.trace("updateAssignment - finished well");
    validator.validate(assignment);

    if (!assignmentRepository.existsById(id)) return false;

    Assignment update = assignmentRepository.findById(id).get();
    update.setGrade(assignment.getGrade());
    update.setStudentId(assignment.getStudentId());
    update.setLabProblemId(assignment.getLabProblemId());

    return true;
  }

  @Override
  public boolean deleteAssignment(Long id) {
    // todo log
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      log.trace("deleteAssignment - method entered: assignment={}", id);
      log.debug("addAssignment - delete: a={}", id);
      Optional<Assignment> entity = assignmentRepository.findById(id);
      if (entity.isEmpty()) return false;
      assignmentRepository.deleteById(id);
      log.trace("addAssignment - finished well");
      return true;
    } catch (EmptyResultDataAccessException e) {
      log.trace("addAssignment - finished bad");
      return false;
    }
  }

  @Override
  public Assignment getAssignmentById(Long id) {
    try {
      return assignmentRepository.getOne(id);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  @Override
  public AbstractMap.SimpleEntry<Long, Double> greatestMean() {
    Iterable<Assignment> assignmentIterable = assignmentRepository.findAll();
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

  @Override
  public AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = assignmentRepository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    log.trace("idOfLabProblemMostAssigned - method entered");
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

  @Override
  public Double averageGrade() {
    log.trace("averageGrade - method entered");
    Iterable<Assignment> assignmentIterable = assignmentRepository.findAll();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
      return (double) gradesSum / (double) assignments.size();
    } else {
      return null;
    }
  }

  @Override
  public boolean deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    log.trace("deleteStudent - method entered: student id={}", id);
    if (studentService.getStudentById(id) == null) return false;

    List<Assignment> allAssignments = this.getAllAssignments();
    log.debug("deleteStudent - deleted: a={}", id);
    allAssignments.stream()
        .filter(entity -> entity.getStudentId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));
    studentService.deleteStudent(id);
    return true;
  }

  @Override
  public boolean deleteLabProblem(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    log.trace("deleteLabProblem - method entered: delete={}", id);
    if (labProblemService.getLabProblem(id) == null) return false;
    log.debug("deleteLabProblem - deleted: a={}", id);
    List<Assignment> allAssignments = this.getAllAssignments();

    allAssignments.stream()
        .filter(entity -> entity.getLabProblemId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));

    labProblemService.deleteLabProblem(id);
    return true;
  }
}
