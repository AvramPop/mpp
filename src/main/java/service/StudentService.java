package service;

import domain.Student;
import domain.exceptions.ValidatorException;
import repository.Repository;

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
}
