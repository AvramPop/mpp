package ro.ubb.springjpa.service;

import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.Assignment;
import ro.ubb.springjpa.model.LabProblem;
import ro.ubb.springjpa.model.Student;
import ro.ubb.springjpa.service.sort.Sort;

import java.util.*;

public interface AssignmentService {
  Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException;

  Set<Assignment> getAllAssignments();

  /** Return all Assignments sorted by the sort criteria. */
  List<Assignment> getAllAssignmentsSorted(Sort sort);

  /**
   * Get Optional containing assignment with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find assignment by
   * @return Optional containing the sought Assignment or null otherwise
   */
  Optional<Assignment> getAssignmentById(Long id);

  /**
   * Deletes an assignment from the ro.ubb.repository
   *
   * @param id the id of the assignment to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  Optional<Assignment> deleteAssignment(Long id);

  Optional<Assignment> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException;

  /**
   * Returns the student id who has the biggest mean of grades
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade
   *     average
   */
  Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean();

  /**
   * Returns the id of the lab problem which was assigned the most often
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
   *     {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of
   *     assignments
   */
  Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned();

  /**
   * Returns the average grade of all the groups
   *
   * @return an {@code Optional} containing a null if no student is in the repository otherwise a
   *     {@code Double} which represents the average grade
   */
  Optional<Double> averageGrade();

  /**
   * Return a mapping of every Student and a list of it's assigned LabProblems.
   *
   * @return the sought Student - List of LabProblems. If student has no assignment, map to an empty
   *     list.
   */
  Optional<Map<Student, List<LabProblem>>> studentAssignedProblems();
}
