package ro.ubb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.StudentRepository;
import ro.ubb.service.validators.Validator;
import ro.ubb.repository.sort.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ravasz Tamas The controller for the student entities, and for the functionalities which
 *     are performed on these entities
 */
@Service
public class StudentServiceImplementation implements StudentService {
  private static final Logger log = LoggerFactory.getLogger(StudentServiceImplementation.class);
  @Autowired private StudentRepository repository;
  @Autowired private Validator<Student> validator;

  /**
   * Updates a student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param serialNumber serial number of entity to be updated
   * @param name name of entity to be updated
   * @param group group number of entity to be updated
   * @return a null if successfully added or the entity passed to
   *     the ro.ubb.repository otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  @Override
  public Student addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    log.trace("addStudent - method entered: id={} serialNumber={}, name={}, group={}", id,serialNumber,name,group);
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    validator.validate(newStudent);
    log.trace("addStudent - student validated");

    Student result = repository.save(newStudent);
    log.trace("addStudent - method finished: result={}",result);
    return result;
  }

  /**
   * Returns all the students in the ro.ubb.repository
   *
   * @return a set of all the students
   */
  @Override
  public List<Student> getAllStudents() {
    log.trace("getAllStudents - method entered");
    List<Student> result = repository.findAll();
    log.trace("getAllStudents - method finished: result={}",result);
    return result;
  }

  /** Return all Students sorted by the sort criteria. */
  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    log.trace("getAllStudentsSorted - method entered");
    Iterable<Student> students = sort.sort(repository.findAll());
    log.trace("getAllStudentsSorted - students sorted");
    List<Student> result = StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
    log.trace("getAllStudentsSorted - method finished: result={}",result);
    return result;
  }

  /**
   * Deletes a student from the ro.ubb.repository
   *
   * @param id the id of the student to be deleted
   * @return * @return an {@code Optional} containing a null if successfully deleted otherwise the
   *     entity passed to the repository
   */
  @Override
  public void deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    log.trace("deleteStudent - method entered: id={}", id);
    repository.deleteById(id);
    log.trace("deleteStudent - method finished");

  }

  /**
   * Updates a student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param serialNumber serial number of entity to be updated
   * @param name name of entity to be updated
   * @param group group number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity passed to
   *     the ro.ubb.repository otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  @Override
  @Transactional
  public void updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    log.trace("updateStudent - method entered: id={} serialNumber={}, name={}, group={}", id,serialNumber,name,group);
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    log.trace("updateStudent - student validated");

    repository
        .findById(student.getId())
        .ifPresent(
            s -> {
              s.setName(student.getName());
              s.setGroupNumber(student.getGroupNumber());
              s.setSerialNumber(student.getSerialNumber());
              log.debug("updateStudent - updated: s={}", s);
            });
    log.trace("updateStudent - method finished");
  }

  /**
   * Filters the elements of the ro.ubb.repository by a given group number
   *
   * @param group the group number to be filtered by
   * @return a {@code Set} - of entities filtered by the given group number
   */
  @Override
  public Set<Student> filterByGroup(Integer group) {
    if (group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    log.trace("filterByGroup - method entered: group={}", group);
    Iterable<Student> students = repository.findAll();
    Set<Student> filteredStudents = new HashSet<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getGroupNumber() != group);
    log.debug("filterByGroup - method finished: result={}", filteredStudents);
    return filteredStudents;
  }

  /**
   * Get Optional containing student with given id if there is one in the ro.ubb.repository below.
   *
   * @param id to find student by
   * @return Optional containing the sought Student or null otherwise
   */
  @Override
  public Student getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    log.trace("getStudentById - method entered: id={}", id);
    Student student = repository.findById(id).orElse(null);
    log.debug("getStudentById - method exit: student={}", student);
    return student;
  }
}
