package ro.ubb.socket.common.service;


import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

public interface StudentService {
  Future<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException;

  Future<Set<Student>> getAllStudents();

  /**
   * Return all Students sorted by the sort criteria.
   */
  Future<List<Student>> getAllStudentsSorted(Sort sort);

  /**
   * Get Optional containing student with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find student by
   * @return Optional containing the sought Student or null otherwise
   */
  Future<Student> getStudentById(Long id);

  /**
   * Updates an student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  Future<Student> updateStudent(Long id, String serialNumber, String name, int group) throws ValidatorException;
}
