package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.StudentRepository;
import ro.ubb.catalog.core.service.sort.Sort;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Created by radu. */
@Service
public class StudentServiceImpl implements StudentService {
  public static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

  @Autowired private StudentRepository studentRepository;

  @Override
  public List<Student> getAllStudents() {
    log.trace("getAllStudents --- method entered");

    List<Student> result = studentRepository.findAll();

    log.trace("getAllStudents: result={}", result);

    return result;
  }

  @Override
  public boolean saveStudent(Student student) {
    log.trace("saveStudent --- method entered");
    if (studentRepository.existsById(student.getId())) return false;
    studentRepository.save(student);
    return true;
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    log.trace("getAllStudentsSorted - method entered");

    Iterable<Student> students = studentRepository.findAll();
    Iterable<Student> studentsSorted = sort.sort(students);
    log.trace("getAllStudentsSorted - finished well");

    return StreamSupport.stream(studentsSorted.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<Student> filterByGroup(Integer group) {
    if (group == null || group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    log.trace("filterByGroup - method entered");
    Iterable<Student> students = studentRepository.findAll();
    List<Student> filteredStudents = new ArrayList<>();
    students.forEach(filteredStudents::add);
    filteredStudents.removeIf(entity -> entity.getGroupNumber() != group);
    log.trace("filterByGroup - finished well");
    return filteredStudents;
  }

  @Override
  @Transactional
  public boolean updateStudent(Long id, Student student) {
    log.trace("updateStudent - method entered");
    if (!studentRepository.existsById(id)) return false;

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
}
