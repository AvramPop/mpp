package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;
import ro.ubb.socket.server.repository.SortingRepository;
import ro.ubb.socket.server.service.validators.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Ravasz Tamas The controller for the student entities, and for the functionalities which
 *     are performed on these entities
 */
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

  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    return repository.delete(id);
  }

  @Override
  public Future<Boolean> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    validator.validate(newStudent);

    return executorService.submit(() -> repository.save(newStudent).isEmpty());
  }

  @Override
  public Future<Boolean> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    return executorService.submit(() -> repository.update(student).isEmpty());
  }

  @Override
  public Future<Set<Student>> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return executorService.submit(
        () -> StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet()));
  }

  @Override
  public Future<List<Student>> getAllStudentsSorted(Sort sort) {
    Iterable<Student> students = repository.findAll(sort);
    return executorService.submit(
        () -> StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList()));
  }

  @Override
  public Future<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return executorService.submit(() -> repository.findOne(id).get());
  }


}
