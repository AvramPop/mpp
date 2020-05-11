package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.StudentRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.LabProblemValidator;
import ro.ubb.catalog.core.service.validator.StudentValidator;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.domain.PageRequest.of;

/** Created by radu. */
@Service
public class StudentServiceImpl implements StudentService {
  public static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

  @Autowired private StudentRepository studentRepository;
  @Autowired private StudentValidator validator;

  @Override
  public List<Student> getAllStudents() {
    log.trace("getAllStudents --- method entered");

    List<Student> result = studentRepository.findAll();

    log.trace("getAllStudents: result={}", result);

    return result;
  }

  @Override
  public Page<Student> getAllStudents(int pageNumber, int perPage){
    log.trace("getAllStudents paginated --- method entered, {}", pageNumber);
    Pageable pageable = of(pageNumber, perPage);
    return studentRepository.findAll(pageable);
  }

  @Override
  public boolean saveStudent(Student student) {
    log.trace("saveStudent --- method entered");
    validator.validate(student);

    if (studentRepository.existsById(student.getId())) return false;
    studentRepository.save(student);
    return true;
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
//    log.trace("getAllStudentsSorted - method entered");
//
//    Iterable<Student> students = studentRepository.findAll();
//    Iterable<Student> studentsSorted = sort.sort(students);
//    log.trace("getAllStudentsSorted - finished well");
//
//    return StreamSupport.stream(studentsSorted.spliterator(), false).collect(Collectors.toList());


    log.trace("getAllStudentsSorted - method entered");
    org.springframework.data.domain.Sort springSort = null;
    for(int i = 0; i < sort.getSortingChain().size(); i++){
      org.springframework.data.domain.Sort.Direction direction = sort.getSortingChain().get(i).getKey() == Sort.Direction.ASC ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
      if(i == 0){
        springSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
      } else {
        org.springframework.data.domain.Sort newSpringSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
        springSort = springSort.and(newSpringSort);
      }
    }
    Iterable<Student> students = studentRepository.findAll(springSort);
    log.trace("getAllStudentsSorted - finished well");

    return StreamSupport.stream(students.spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public List<Student> filterByGroup(Integer group) {
    if (group == null || group < 0) {
      throw new IllegalArgumentException("group negative!");
    }
    log.trace("filterByGroup - method entered");
//    Iterable<Student> students = studentRepository.findAll();
//    List<Student> filteredStudents = new ArrayList<>();
//    students.forEach(filteredStudents::add);
//    filteredStudents.removeIf(entity -> entity.getGroupNumber() != group);
    List<Student> filteredStudents = studentRepository.findByGroupNumber(group);
    log.trace("filterByGroup - finished well");
    return filteredStudents;
  }

  @Override
  @Transactional
  public boolean updateStudent(Long id, Student student) {
    log.trace("updateStudent - method entered");
    if (!studentRepository.existsById(id)) return false;
    validator.validate(student);

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
