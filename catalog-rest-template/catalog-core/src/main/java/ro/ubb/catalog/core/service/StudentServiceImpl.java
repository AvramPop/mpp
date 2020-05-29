package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.StudentRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.AssignmentValidator;
import ro.ubb.catalog.core.service.validator.LabProblemValidator;
import ro.ubb.catalog.core.service.validator.StudentValidator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.domain.PageRequest.of;

/** Created by radu. */
@Service
public class StudentServiceImpl implements StudentService {
  public static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentValidator validator;
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired private AssignmentValidator assignmentValidator;

  @Override
  public List<Student> getAllStudents() {
//    log.trace("getAllStudents --- method entered");

    List<Student> result = studentRepository.findAll();

//    log.trace("getAllStudents: result={}", result);

    return result;
  }

  @Override
  public Page<Student> getAllStudents(int pageNumber, int perPage){
//    log.trace("getAllStudents paginated --- method entered, {}", pageNumber);
    Pageable pageable = of(pageNumber, perPage);
    return studentRepository.findAll(pageable);
  }

  @Override
  public boolean saveStudent(Student student) {
    log.trace("saveStudent --- method entered");
    validator.validate(student);

    if (studentRepository.existsById(student.getId())) return false;
    studentRepository.save(student);
    return true;
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
//    log.trace("getAllStudentsSorted - method entered");
//
//    Iterable<Student> students = studentRepository.findAll();
//    Iterable<Student> studentsSorted = sort.sort(students);
//    log.trace("getAllStudentsSorted - finished well");
//
//    return StreamSupport.stream(studentsSorted.spliterator(), false).collect(Collectors.toList());


    log.trace("getAllStudentsSorted - method entered");
    org.springframework.data.domain.Sort springSort = null;
    for(int i = 0; i < sort.getSortingChain().size(); i++){
      org.springframework.data.domain.Sort.Direction direction = sort.getSortingChain().get(i).getKey() == Sort.Direction.ASC ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
      if(i == 0){
        springSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
      } else {
        org.springframework.data.domain.Sort newSpringSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
        springSort = springSort.and(newSpringSort);
      }
    }
    Iterable<Student> students = studentRepository.findAll(springSort);
    log.trace("getAllStudentsSorted - finished well");

    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<Student> filterByGroup(Integer group) {
    if (group == null || group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    log.trace("filterByGroup - method entered");
//    Iterable<Student> students = studentRepository.findAll();
//    List<Student> filteredStudents = new ArrayList<>();
//    students.forEach(filteredStudents::add);
//    filteredStudents.removeIf(entity -> entity.getGroupNumber() != group);
    List<Student> filteredStudents = studentRepository.findByGroupNumber(group);
    log.trace("filterByGroup - finished well");
    return filteredStudents;
  }

  @Override
  @Transactional
  public boolean updateStudent(Long id, Student student) {
    log.trace("updateStudent - method entered");
    if (!studentRepository.existsById(id)) return false;
    validator.validate(student);

    Student update = studentRepository.findById(id).get();
    update.setSerialNumber(student.getSerialNumber());
    update.setName(student.getName());
    update.setGroupNumber(student.getGroupNumber());

    return true;
  }

  @Override
  public boolean deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      log.trace("deleteStudent - method entered: id={}", id);
      Optional<Student> entity = studentRepository.findById(id);
      if (entity.isEmpty()) return false;

      studentRepository.deleteById(id);
      log.trace("addAssignment - finished well");
      return true;
    } catch (EmptyResultDataAccessException e) {
      log.trace("addAssignment - finished bad");
      return false;
    }
  }

  @Override
  public Student getStudentById(Long id) {
    try {
      return studentRepository.getOne(id);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

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
    assignmentValidator.validate(assignment);
    //    assignmentRepository.save(assignment);
    studentRepository.findAllWithAssignments().stream()
        .filter(student -> student.getId().equals(assignment.getStudent().getId()))
        .findFirst()
        .get()
        .getAssignments()
        .add(assignment);
    return true;
    }

  @Override
  public List<Student> findByGroupNumberCustom(int groupNumber){
    return studentRepository.findByGroupNumberCustom(groupNumber);
  }

  @Override
  public List<Student> findByNameCustom(String name){
    return studentRepository.findByNameCustom(name);
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
    assignmentValidator.validate(assignment);

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
//  @Transactional
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
    entityManager.flush();
    return true;
  }



  @Override
  public AbstractMap.SimpleEntry<Long, Double> greatestMean() {
    Iterable<Assignment> assignmentIterable = this.getAllAssignments();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    log.trace("greatestMean - method entered");
    return studentRepository.findAll().stream()
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
  public Double averageGrade() {
    log.trace("averageGrade - method entered");
    Iterable<Assignment> assignmentIterable = getAllAssignments();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);

    if (assignments.size() > 0) {
      return (double) gradesSum / (double) assignments.size();
    } else {
      return null;
    }
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
//  public boolean deleteAssignment(Long id) {
//        entityManager.getTransaction().begin();
//        Assignment entity = entityManager.find(Assignment.class, id);
//        entityManager.remove(entity);
//
//        entityManager.getTransaction().commit();
//        entityManager.flush();
//        getAllAssignments();
//        return true;
//  }

}
