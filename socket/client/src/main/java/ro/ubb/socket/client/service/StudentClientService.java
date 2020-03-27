package ro.ubb.socket.client.service;

import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public class StudentClientService implements StudentService {

  @Override
  public Future<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    return null;
  }

  @Override
  public Future<Set<Student>> getAllStudents() {
    return null;
  }

  @Override
  public Future<List<Student>> getAllStudentsSorted(Sort sort) {
    return null;
  }

  @Override
  public Future<Student> getStudentById(Long id) {
    return null;
  }

  @Override
  public Future<Student> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    return null;
  }

  @Override
  public Optional<Student> deleteStudent(Long id){ // this is not needed here but necessary for interface contract
    return Optional.empty();
  }
}
