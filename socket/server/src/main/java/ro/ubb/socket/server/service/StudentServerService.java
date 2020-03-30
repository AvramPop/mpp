package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;
import ro.ubb.socket.server.repository.SortingRepository;
import ro.ubb.socket.server.service.validators.Validator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Server service to handle Student specific data communicated via socket. */
public class StudentServerService implements StudentService {
  private SortingRepository<Long, Student> repository;
  private Validator<Student> validator;
  private ExecutorService executorService;

  public StudentServerService(
      SortingRepository<Long, Student> repository,
      Validator<Student> validator,
      ExecutorService executorService) {
    this.repository = repository;
    this.validator = validator;
    this.executorService = executorService;
  }

  /**
   * Deletes the student with the id from the database
   *
   * @param id the id of the student to be deleted
   * @return a future containing true if the operation is successfull otherwise false
   */
  @Override
  public Future<Boolean> deleteStudent(Long id) {

    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return executorService.submit(() -> repository.delete(id).isPresent());
  }
  /**
   * Performs the saving of the new entity to the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    validator.validate(newStudent);

    return executorService.submit(() -> repository.save(newStudent).isEmpty());
  }
  /**
   * Performs the update of a student entity in the database
   *
   * @return Future containing the truth value of the success of the operation
   * @throws ValidatorException if the given parameters are invalid
   */
  @Override
  public Future<Boolean> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    return executorService.submit(() -> repository.update(student).isEmpty());
  }

  /**
   * Returns all students from the database
   *
   * @return a future containing a set of elements
   */
  @Override
  public Future<Set<Student>> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return executorService.submit(
        () -> StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet()));
  }
  /**
   * Returns all students from the database sorted by the sort entity
   *
   * @return a future containing a set of elements sorted
   */
  @Override
  public Future<List<Student>> getAllStudentsSorted(Sort sort) {
    Iterable<Student> students = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList()));
  }

  /**
   * Returns a student given by the id
   *
   * @param id to find student by
   * @return a Future containing the entity, or null of the entity doesn't exist
   */
  @Override
  public Future<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());
  }
}
