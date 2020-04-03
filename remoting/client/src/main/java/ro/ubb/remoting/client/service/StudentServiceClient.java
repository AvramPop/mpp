package ro.ubb.remoting.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.List;
import java.util.Set;

/** Created by radu. */
public class StudentServiceClient implements StudentService {
  @Autowired private StudentService studentService;

  @Override
  public Student addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    return studentService.addStudent(id, serialNumber, name, group);
  }

  @Override
  public Set<Student> getAllStudents() {
    return studentService.getAllStudents();
  }

  @Override
  public List<Student> getAllStudentsSorted(Sort sort) {
    return studentService.getAllStudentsSorted(sort);
  }

  @Override
  public Student deleteStudent(Long id) {
    return studentService.deleteStudent(id);
  }

  @Override
  public Student updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    return studentService.updateStudent(id, serialNumber, name, group);
  }

  @Override
  public Set<Student> filterByGroup(Integer group) {
    return studentService.filterByGroup(group);
  }

  @Override
  public Student getStudentById(Long id) {
    return studentService.getStudentById(id);
  }
}
