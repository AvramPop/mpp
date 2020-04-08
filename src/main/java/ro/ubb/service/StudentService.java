package ro.ubb.service;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.sort.Sort;

import java.util.List;
import java.util.Set;

/** Created by radu. */
public interface StudentService {
  /**
   * Updates a student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param serialNumber serial number of entity to be updated
   * @param name name of entity to be updated
   * @param group group number of entity to be updated
   * @return an {@code Optional} containing the null if successfully added or the entity passed to
   *     the ro.ubb.repository otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  Student addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException;
  /**
   * Returns all the students in the ro.ubb.repository
   *
   * @return a set of all the students
   */
  List<Student> getAllStudents();

  /** Return all Students sorted by the sort criteria. */
  List<Student> getAllStudentsSorted(Sort sort);

  /**
   * Deletes a student from the ro.ubb.repository
   *
   * @param id the id of the student to be deleted
   * @return * @return an {@code Optional} containing a null if successfully deleted otherwise the
   *     entity passed to the repository
   */
  void deleteStudent(Long id);

  /**
   * Updates a student inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @param serialNumber serial number of entity to be updated
   * @param name name of entity to be updated
   * @param group group number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity passed to
   *     the ro.ubb.repository otherwise
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  void updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException;

  /**
   * Filters the elements of the ro.ubb.repository by a given group number
   *
   * @param group the group number to be filtered by
   * @return a {@code Set} - of entities filtered by the given group number
   */
  Set<Student> filterByGroup(Integer group);

  /**
   * Get Optional containing student with given id if there is one in the ro.ubb.repository below.
   *
   * @param id to find student by
   * @return Optional containing the sought Student or null otherwise
   */
  Student getStudentById(Long id);
}
