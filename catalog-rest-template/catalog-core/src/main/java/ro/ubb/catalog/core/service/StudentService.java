package ro.ubb.catalog.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.sort.Sort;

import java.util.List;

/** Created by radu. */
@Service
public interface StudentService {
  List<Student> getAllStudents();

  Page<Student> getAllStudents(int pageNumber, int perPage);

  boolean saveStudent(Student student);

  List<Student> getAllStudentsSorted(Sort sort);

  List<Student> filterByGroup(Integer group);

  boolean updateStudent(Long id, Student student);

  boolean deleteStudent(Long id);

  Student getStudentById(Long id);
  List<Student> findByGroupNumberCustom(int groupNumber);
  List<Student> findByNameCustom(String name);
}
