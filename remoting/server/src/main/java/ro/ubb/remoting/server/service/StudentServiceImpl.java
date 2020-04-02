package ro.ubb.remoting.server.service;

import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.common.service.sort.Sort;
import ro.ubb.remoting.server.repository.SortingRepository;
import ro.ubb.remoting.server.service.validators.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Created by radu. */
public class StudentServiceImpl implements StudentService {
  private SortingRepository<Long, Student> repository;
  private Validator<Student> validator;

  public StudentServiceImpl(
      SortingRepository<Long, Student> repository, Validator<Student> validator) {
    this.repository = repository;
    this.validator = validator;
  }

  @Override
  public Optional<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    validator.validate(newStudent);

    return repository.save(newStudent);
  }

  @Override
  public Set<Student> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    Iterable<Student> students = repository.findAll(sort);
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    return repository.delete(id);
  }

  @Override
  public Optional<Student> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    return repository.update(student);
  }

  @Override
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

  @Override
  public Optional<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id);
  }
}
