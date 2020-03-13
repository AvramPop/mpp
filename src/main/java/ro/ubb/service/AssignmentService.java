package ro.ubb.service;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.RepositoryException;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AssignmentService {

  private Repository<Long, Assignment> repository;
  private LabProblemService labProblemService;
  private StudentService studentService;
  private Validator<Assignment> assignmentValidator;

  public AssignmentService(
      Repository<Long, Assignment> repository,
      LabProblemService labProblemService,
      StudentService studentService,
      Validator<Assignment> assignmentValidator) {
    this.repository = repository;
    this.studentService = studentService;
    this.labProblemService = labProblemService;
    this.assignmentValidator = assignmentValidator;
  }

  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      Assignment assignment = new Assignment(studentID, labProblemID, grade);
      assignment.setId(id);
      assignmentValidator.validate(assignment);
      return repository.save(assignment);
    }
    throw new RepositoryException("Invalid assignment");
  }

  public Set<Assignment> getAllAssignments() {
    Iterable<Assignment> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  /**
   * Get Optional containing assignment with given id if there is one in the ro.ubb.repository
   * below.
   *
   * @param id to find assignment by
   * @return Optional containing the sought Assignment or null otherwise
   */
  public Optional<Assignment> getAssignmentById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findOne(id);
  }

  /**
   * Deletes an assignment from the ro.ubb.repository
   *
   * @param id the id of the assignment to be deleted
   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
   *     passed to the repository
   */
  public Optional<Assignment> deleteAssignment(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("Invalid id!");
    }
    return repository.delete(id);
  }

  /**
   * Updates an assignment inside the ro.ubb.repository
   *
   * @param id id number of entity to be updated
   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
   *     the ro.ubb.repository
   * @throws ValidatorException if the object is incorrectly defined by the user
   */
  public Optional<Assignment> updateAssignment(
      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      Assignment assignment = new Assignment(studentID, labProblemID, grade);
      assignment.setId(id);
      assignmentValidator.validate(assignment);
      return repository.update(assignment);
    }
    throw new RepositoryException("Invalid assignment");
  }
}
