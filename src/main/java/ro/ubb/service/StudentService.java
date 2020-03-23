package ro.ubb.service;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.db.sort.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ravasz Tamas The controller for the student entities, and for the functionalities which
 *     are performed on these entities
 */
public class StudentService {
  private SortingRepository<Long, Student> repository;
  private Validator<Student> validator;

  public StudentService(SortingRepository<Long, Student> repository, Validator<Student> validator) {
    this.repository = repository;
    this.validator = validator;
  }

  /**
   * Updates a student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param serialNumber serial number of entity to be updated
   * @param name name of entity to be updated
   * @param group group number of entity to be updated
   * @return an {@code Optional} containing the null if successfully added or the entity passed to
   *     the ro.ubb.repository otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  public Optional<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    validator.validate(newStudent);

    return repository.save(newStudent);
  }

  /**
   * Returns all the students in the ro.ubb.repository
   *
   * @return a set of all the students
   */
  public Set<Student> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
  }

  /**
   * Return all Students sorted by the sort criteria.
   */
  public List<Student> getAllStudentsSorted(Sort sort) {
    Iterable<Student> students = repository.findAll(sort);
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
  }

  /**
   * Deletes a student from the ro.ubb.repository
   *
   * @param id the id of the student to be deleted
   * @return * @return an {@code Optional} containing a null if successfully deleted otherwise the
   *     entity passed to the repository
   */
  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    return repository.delete(id);
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
  public Optional<Student> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    return repository.update(student);
  }

  /**
   * Filters the elements of the ro.ubb.repository by a given group number
   *
   * @param group the group number to be filtered by
   * @return a {@code Set} - of entities filtered by the given group number
   */
  public Set<Student> filterByGroup(Integer group) {
    if (group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    Iterable<Student> students = repository.findAll();
    Set<Student> filteredStudents = new HashSet<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getGroup() != group);
    return filteredStudents;
  }

  /**
   * Get Optional containing student with given id if there is one in the ro.ubb.repository below.
   *
   * @param id to find student by
   * @return Optional containing the sought Student or null otherwise
   */
  public Optional<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id);
  }
}
