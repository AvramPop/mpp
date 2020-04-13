package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.repository.StudentRepository;
import ro.ubb.springjpa.service.sort.Sort;
import ro.ubb.springjpa.service.validator.StudentValidator;
import ro.ubb.springjpa.model.Student;
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
    log.trace("addStudent - method entered: student={}", newStudent);
    newStudent.setId(id);
    if(getStudentById(id).isPresent()) return Optional.of(newStudent);
    validator.validate(newStudent);
    try {
      repository.save(newStudent);
      log.debug("addStudent - updated: a={}", newStudent);
      log.trace("addStudent - finished well");
      return Optional.empty();
    } catch (JpaSystemException e) {
      log.trace("addStudent - finished bad");
      return Optional.of(newStudent);
    }
  }

  @Override
  public Set<Student> getAllStudents() {
    log.trace("getAllStudents - method entered");
    Iterable<Student> students = repository.findAll();
    log.trace("getAllStudents - finished well");
    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    log.trace("getAllStudentsSorted - method entered");

    Iterable<Student> students = repository.findAll();
    Iterable<Student> studentsSorted = sort.sort(students);
    log.trace("getAllStudentsSorted - finished well");

    return StreamSupport.stream(studentsSorted.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public Optional<Student> deleteStudent(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      log.trace("deleteStudent - method entered: id={}", id);
      Optional<Student> entity = repository.findById(id);
      repository.deleteById(id);
      log.trace("addAssignment - finished well");
      return entity;
    } catch (EmptyResultDataAccessException e) {
      log.trace("addAssignment - finished bad");
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
    log.trace("filterByGroup - method entered");
    Iterable<Student> students = repository.findAll();
    Set<Student> filteredStudents = new HashSet<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getStudentGroup() != group);
    log.trace("filterByGroup - finished well");
    return filteredStudents;
  }

  @Override
  public Optional<Student> getStudentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    log.trace("getStudentById - method entered");

    return repository.findById(id);
  }
}
