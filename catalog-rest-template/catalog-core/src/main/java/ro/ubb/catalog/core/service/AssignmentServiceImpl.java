package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.AssignmentRepository;
import ro.ubb.catalog.core.repository.LabProblemRepository;
import ro.ubb.catalog.core.repository.StudentRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.AssignmentValidator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.domain.PageRequest.of;

@Service
public class AssignmentServiceImpl implements AssignmentService {
  public static final Logger log = LoggerFactory.getLogger(AssignmentServiceImpl.class);

  @Autowired private AssignmentRepository assignmentRepository;
  @Autowired private StudentService studentService;
  @Autowired private LabProblemService labProblemService;
  @Autowired private AssignmentValidator validator;
  @Autowired private StudentRepository studentRepository;
  @Autowired private LabProblemRepository labProblemRepository;

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Assignment> getAllAssignments() {
    log.trace("getAllAssignments --- method entered");

    List<Assignment> result =
        studentRepository.findAllWithAssignments().stream()
            .map(Student::getAssignments)
            .reduce(
                new ArrayList<>(),
                (a, b) -> {
                  a.addAll(b);
                  return a;
                });

    log.trace("getAllAssignments: result={}", result);

    return result;
  }

  @Override
  @Transactional
  public boolean saveAssignment(Long id, Long studentId, Long labProblemId, int grade) {
    Assignment assignment =
        new Assignment(
            grade,
            entityManager.getReference(Student.class, studentId),
            entityManager.getReference(LabProblem.class, labProblemId));
    assignment.setId(id);
    validator.validate(assignment);
    if (assignmentRepository.existsById(assignment.getId())) return false;
    //    assignmentRepository.save(assignment);
    studentRepository.findAllWithAssignments().stream()
        .filter(student -> student.getId().equals(assignment.getStudent().getId()))
        .findFirst()
        .get()
        .getAssignments()
        .add(assignment);
    labProblemRepository.findAllLabProblemsWithAssignments().stream()
        .filter(labProblem -> labProblem.getId().equals(assignment.getLabProblem().getId()))
        .findFirst()
        .get()
        .getAssignments()
        .add(assignment);
    return true;
  }

  @Override
  @Transactional
  public boolean updateAssignment(Long id, Long studentId, Long labProblemId, int grade) {
    // todo log
    Assignment assignment =
        new Assignment(
            grade,
            entityManager.getReference(Student.class, studentId),
            entityManager.getReference(LabProblem.class, labProblemId));
    assignment.setId(id);
    log.trace("updateAssignment - finished well");
    validator.validate(assignment);

    if (!assignmentRepository.existsById(id)) return false;
    studentRepository.findAllWithAssignments().stream()
        .filter(studentTemp -> studentTemp.getId().equals(assignment.getStudent().getId()))
        .findFirst()
        .get()
        .getAssignments()
        .stream()
        .filter(entity -> entity.getId().equals(assignment.getId()))
        .forEach(
            as -> {
              as.setStudent(
                  entityManager.getReference(Student.class, assignment.getStudent().getId()));
              as.setLabProblem(
                  entityManager.getReference(LabProblem.class, assignment.getLabProblem().getId()));
              as.setGrade(assignment.getGrade());
            });
    return true;
  }

  @Override
  @Transactional
  public boolean deleteAssignment(Long id) {
    // todo log
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    Optional<Assignment> optionalAssignment =
        studentRepository.findAllWithAssignments().stream()
            .map(student -> student.getAssignments())
            .flatMap(rentals -> rentals.stream())
            .filter(assignment -> assignment.getId().equals(id))
            .findFirst();
    optionalAssignment.ifPresent(
        assignment -> {
          Optional<Student> student =
              studentRepository.findByIdWithAssignments(assignment.getStudent().getId());
          student.ifPresent(studentEntity -> studentEntity.getAssignments().remove(assignment));
        });
    return true;
  }

  //  @Override
//  @Transactional
//  public boolean deleteAssignment(Long id) {
  //    try {
  //      log.trace("deleteAssignment - method entered: assignment={}", id);
  //      log.debug("deleteAssignment - delete: a={}", id);
  //      Optional<Assignment> assignment = studentRepository.findAllWithAssignments()
  //          .stream()
  //          .filter(studentTemp -> studentTemp.getAssignments().stream().anyMatch(entity ->
  // entity.getId().equals(id)))
  //          .findFirst().get()
  //          .getAssignments()
  //          .stream()
  //          .filter(entity -> entity.getId().equals(id)).findFirst();
  //      if (assignment.isEmpty()) return false;
  //      System.out.println(assignment.get());
  //      List<Assignment> assignments = studentRepository.findAllWithAssignments()
  //          .stream()
  //          .filter(studentTemp ->
  // studentTemp.getId().equals(assignment.get().getStudent().getId()))
  //          .findFirst().get()
  //          .getAssignments();
  //      assignments.removeIf(entity -> entity.getId().equals(id));
  //      studentRepository.findAllWithAssignments()
  //          .stream()
  //          .filter(studentTemp ->
  // studentTemp.getId().equals(assignment.get().getStudent().getId()))
  //          .findFirst().get().setAssignments(assignments);
  //      assignments = studentRepository.findAllWithAssignments()
  //          .stream()
  //          .filter(studentTemp ->
  // studentTemp.getId().equals(assignment.get().getStudent().getId()))
  //          .findFirst().get()
  //          .getAssignments();
  //      log.trace("---------- teoretic updatat");
  //
  //      assignments.forEach(System.out::println);
  //      log.trace("deleteAssignment - finished well");
  //      return true;
  //    } catch (EmptyResultDataAccessException e) {
  //      log.trace("deleteAssignment - finished bad");
  //      return false;
  //    }
//  }

//  @Override
//  @Transactional
//  public boolean deleteAssignment(Long id) {
    //      entityManager.getTransaction().begin();
    //      Assignment entity = entityManager.find(Assignment.class, 4L);
    //      entityManager.remove(entity);
    //      entityManager.getTransaction().commit();
    //      return true;
//  }

  @Override
  public Assignment getAssignmentById(Long id) {
    try {
      return assignmentRepository.getOne(id);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  @Override
  public Page<Assignment> getAllAssignments(int pageNumber, int perPage) {
    log.trace("getAllAssignments paginated --- method entered, {}", pageNumber);
    Pageable pageable = of(pageNumber, perPage);
    return assignmentRepository.findAll(pageable);
  }

  @Override
  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
    org.springframework.data.domain.Sort springSort = null;
    for (int i = 0; i < sort.getSortingChain().size(); i++) {
      org.springframework.data.domain.Sort.Direction direction =
          sort.getSortingChain().get(i).getKey() == Sort.Direction.ASC
              ? org.springframework.data.domain.Sort.Direction.ASC
              : org.springframework.data.domain.Sort.Direction.DESC;
      if (i == 0) {
        springSort =
            new org.springframework.data.domain.Sort(
                direction, sort.getSortingChain().get(i).getValue());
      } else {
        org.springframework.data.domain.Sort newSpringSort =
            new org.springframework.data.domain.Sort(
                direction, sort.getSortingChain().get(i).getValue());
        springSort = springSort.and(newSpringSort);
      }
    }
    Iterable<Assignment> assignments = assignmentRepository.findAll(springSort);
    log.trace("getAllAssignmentsSorted - finished well");
    return StreamSupport.stream(assignments.spliterator(), false).collect(Collectors.toList());
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
                    .anyMatch(
                        assignment -> assignment.getStudent().getId().equals(student.getId())))
        .map(
            student ->
                new AbstractMap.SimpleEntry<>(
                    student.getId(),
                    (double)
                            assignments.stream()
                                .filter(
                                    assignment ->
                                        assignment.getStudent().getId().equals(student.getId()))
                                .map(Assignment::getGrade)
                                .reduce(0, Integer::sum)
                        / (double)
                            assignments.stream()
                                .filter(
                                    assignment ->
                                        assignment.getStudent().getId().equals(student.getId()))
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
                            assignment ->
                                assignment.getLabProblem().getId().equals(labProblem.getId()))
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
        .filter(entity -> entity.getStudent().getId().equals(id))
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
        .filter(entity -> entity.getLabProblem().getId().equals(id))
        .forEach(entity -> this.deleteAssignment(entity.getId()));

    labProblemService.deleteLabProblem(id);
    return true;
  }
}
