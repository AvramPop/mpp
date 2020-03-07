package service;

import domain.Student;
import domain.exceptions.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ravasz Tamas The controller for the student entities, and for the functionalities which
 *     are performed on these entities
 */
public class StudentService {
  private Repository<Long, Student> repository;

  public StudentService(Repository<Long, Student> repository) {
    this.repository = repository;
  }

  /**
   * Adds a new student to the repository
   *
   * @param student the new entity to be added
   * @throws ValidatorException in the case that the student entity is invalid, this is verified by
   *     the student validator
   */
  public void addStudent(Student student) throws ValidatorException {
    repository.save(student);
  }

  /**
   * Returns all the students in the repository
   *
   * @return a set of all the students
   */
  public Set<Student> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
  }

  /**
   * Deletes a student from the repository
   *
   * @param id the id of the student to be deleted
   */
  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    return repository.delete(id);
  }

  /**
   * Updates an existing entity in the repository with the same id
   *
   * @param student the student to be updated
   * @return an {@code Optional} - null if the enti ty was updated otherwise (e.g. id does not exist)
   *     * returns the entity.
   */
  public Optional<Student> updateStudent(Student student) throws ValidatorException {

    return repository.update(student);
  }

  /**
   * Filters the elements of the repository by a given group number
   *
   * @param group the group number to be filtered by
   * @return a {@code Set} - of entities filtered by the given group number
   */
  public Set<Student> filterByGroup(Integer group) {
    if(group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    Iterable<Student> students = repository.findAll();
    Set<Student> filteredStudents = new HashSet<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getGroup() != group);
    return filteredStudents;
  }

  /**
   * Get Optional containing student with given id if there is one in the repository below.
   * @param id to find student by
   * @return Optional containing the sought Student or null otherwise
   */
  public Optional<Student> getStudentById(Long id) {
    if(id == null || id < 0) {
      throw new IllegalArgumentException("negative id!");
    }
    return repository.findOne(id);
  }
}
