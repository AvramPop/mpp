package ro.ubb.socket.common.service;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public interface AssignmentService {
  Future<Boolean> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException;

  Future<Set<Assignment>> getAllAssignments();

  /** Return all Assignments sorted by the sort criteria. */
  Future<List<Assignment>> getAllAssignmentsSorted(Sort sort);

  /**
   * Get Optional containing assignment with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find assignment by
   * @return Optional containing the sought Assignment or null otherwise
   */
  Future<Assignment> getAssignmentById(Long id);

  /**
   * Deletes an assignment from the ro.ubb.repository
   *
   * @param id the id of the assignment to be deleted
   * @return an {@code Future} - null if there is no entity with the given id, otherwise the
   *     removed entity.
   */
  Future<Boolean> deleteAssignment(Long id);


  /**
   * Updates an assignment inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  Future<Boolean> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException;

  /**
   * Returns the student id who has the biggest mean of grades
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade
   *     average
   */
  Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean();

  /**
   * Returns the id of the lab problem which was assigned the most often
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of
   *     assignments
   */
  Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned();

  /**
   * Returns the average grade of all the groups
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise a
   *     {@code Double} which represents the average grade
   */
  Future<Double> averageGrade();
  /**
   * Return a mapping of every Student and a list of it's assigned LabProblems.
   *
   * @return the sought Student - List of LabProblems. If student has no assignment, map to an empty
   *     list.
   */
  Future<Map<Student, List<LabProblem>>> studentAssignedProblems();
}
