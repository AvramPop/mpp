package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.Student;
import ro.ubb.springjpa.repository.StudentRepository;
import ro.ubb.springjpa.service.sort.Sort;
import ro.ubb.springjpa.service.validator.StudentValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentServiceImpl implements StudentService {
  public static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);
  @Autowired private StudentRepository repository;
  @Autowired private StudentValidator validator;

  @Override
  public Optional<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);
    if(getStudentById(id).isPresent()) return Optional.of(newStudent);
    validator.validate(newStudent);
    try {
      repository.save(newStudent);
      return Optional.empty();
    } catch (JpaSystemException e) {
      return Optional.of(newStudent);
    }
  }

  @Override
  public Set<Student> getAllStudents() {
    Iterable<Student> students = repository.findAll();
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    Iterable<Student> students = repository.findAll();
    Iterable<Student> studentsSorted = sort.sort(students);
    return StreamSupport.stream(studentsSorted.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      Optional<Student> entity = repository.findById(id);
      repository.deleteById(id);
      return entity;
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public Optional<Student> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student student = new Student(serialNumber, name, group);
    student.setId(id);
    validator.validate(student);
    if(getStudentById(id).isEmpty()) return Optional.of(student);

    try {
      log.trace("updateStudent - method entered: student={}", student);
      repository
          .findById(student.getId())
          .ifPresent(
              s -> {
                s.setName(student.getName());
                s.setStudentGroup(student.getStudentGroup());
                s.setSerialNumber(student.getSerialNumber());
                log.debug("updateStudent - updated: s={}", s);
              });
      log.trace("updateStudent - method finished");
      return Optional.empty();
    } catch (JpaSystemException e) {
      return Optional.of(student);
    }
  }

  @Override
  public Set<Student> filterByGroup(Integer group) {
    if (group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    Iterable<Student> students = repository.findAll();
    Set<Student> filteredStudents = new HashSet<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getStudentGroup() != group);
    return filteredStudents;
  }

  @Override
  public Optional<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findById(id);
  }
}
