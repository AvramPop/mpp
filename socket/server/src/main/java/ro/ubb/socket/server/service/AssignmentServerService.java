package ro.ubb.socket.server.service;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class AssignmentServerService implements AssignmentService {
  @Override
  public Future<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException{
    return null;
  }

  @Override
  public Future<Set<Assignment>> getAllAssignments(){
    return null;
  }

  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort){
    return null;
  }

  @Override
  public Future<Assignment> getAssignmentById(Long id){
    return null;
  }

  @Override
  public Future<Assignment> deleteAssignment(Long id){
    return null;
  }

  @Override
  public Future<Student> deleteStudent(Long id){
    return null;
  }

  @Override
  public Future<LabProblem> deleteLabProblem(Long id){
    return null;
  }

  @Override
  public Future<Assignment> updateAssignment(Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException{
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean(){
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned(){
    return null;
  }

  @Override
  public Future<Double> averageGrade(){
    return null;
  }

  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems(){
    return null;
  }
//
//  private SortingRepository<Long, Assignment> repository;
//  private LabProblemService labProblemService;
//  private StudentService studentService;
//  private Validator<Assignment> assignmentValidator;
//
//  public AssignmentService(
//      SortingRepository<Long, Assignment> repository,
//      LabProblemService labProblemService,
//      StudentService studentService,
//      Validator<Assignment> assignmentValidator) {
//    this.repository = repository;
//    this.studentService = studentService;
//    this.labProblemService = labProblemService;
//    this.assignmentValidator = assignmentValidator;
//  }
//
//  public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
//      throws ValidatorException {
//    Assignment assignment = new Assignment(studentID, labProblemID, grade);
//    assignment.setId(id);
//    assignmentValidator.validate(assignment);
//
//    if (studentService.getStudentById(studentID).isPresent()
//        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
//      return repository.save(assignment);
//    }
//    return Optional.of(assignment);
//  }
//
//  public Set<Assignment> getAllAssignments() {
//    Iterable<Assignment> problems = repository.findAll();
//    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
//  }
//
//  /**
//   * Return all Assignments sorted by the sort criteria.
//   */
//  public List<Assignment> getAllAssignmentsSorted(Sort sort) {
//    Iterable<Assignment> problems = repository.findAll(sort);
//    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toList());
//  }
//
//  /**
//   * Get Optional containing assignment with given id if there is one in the ro.ubb.repository
//   * below.
//   *
//   * @param id to find assignment by
//   * @return Optional containing the sought Assignment or null otherwise
//   */
//  public Optional<Assignment> getAssignmentById(Long id) {
//    if (id == null || id < 0) {
//      throw new IllegalArgumentException("invalid id!");
//    }
//    return repository.findOne(id);
//  }
//
//  /**
//   * Deletes an assignment from the ro.ubb.repository
//   *
//   * @param id the id of the assignment to be deleted
//   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
//   *     passed to the repository
//   */
//  public Optional<Assignment> deleteAssignment(Long id) {
//    if (id == null || id < 0) {
//      throw new IllegalArgumentException("Invalid id!");
//    }
//    return repository.delete(id);
//  }
//
//  /**
//   * Deletes a student from the ro.ubb.repository and also deletes all assignments corresponding to
//   * that lab problem
//   *
//   * @param id the id of the student to be deleted
//   * @return * @return an {@code Optional} containing a null if successfully deleted otherwise the
//   *     entity passed to the repository
//   */
//  public Optional<Student> deleteStudent(Long id) {
//    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
//
//    if (studentService.getStudentById(id).isEmpty()) return Optional.empty();
//
//    Set<Assignment> allAssignments = this.getAllAssignments();
//
//    allAssignments.stream()
//        .filter(entity -> entity.getStudentId().equals(id))
//        .forEach(entity -> this.deleteAssignment(entity.getId()));
//
//    return studentService.deleteStudent(id);
//  }
//  /**
//   * Deletes a lab problem from the ro.ubb.repository and also deletes all assignments corresponding
//   * to that student
//   *
//   * @param id the id of the lab problem to be deleted
//   * @return an {@code Optional} containing a null if successfully deleted otherwise the entity
//   *     passed to the repository
//   */
//  public Optional<LabProblem> deleteLabProblem(Long id) {
//    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
//
//    if (labProblemService.getLabProblemById(id).isEmpty()) return Optional.empty();
//
//    Set<Assignment> allAssignments = this.getAllAssignments();
//
//    allAssignments.stream()
//        .filter(entity -> entity.getLabProblemId().equals(id))
//        .forEach(entity -> this.deleteAssignment(entity.getId()));
//
//    return labProblemService.deleteLabProblem(id);
//  }
//
//  /**
//   * Updates an assignment inside the ro.ubb.repository
//   *
//   * @param id id number of entity to be updated
//   * @return an {@code Optional} containing the null if successfully updated or the entity sent to
//   *     the ro.ubb.repository
//   * @throws ValidatorException if the object is incorrectly defined by the user
//   */
//  public Optional<Assignment> updateAssignment(
//      Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException {
//    Assignment assignment = new Assignment(studentID, labProblemID, grade);
//    assignment.setId(id);
//
//    if (studentService.getStudentById(studentID).isPresent()
//        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
//      assignmentValidator.validate(assignment);
//      return repository.update(assignment);
//    }
//    return Optional.of(assignment);
//  }
//
//  /**
//   * Returns the student id who has the biggest mean of grades
//   *
//   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
//   *     {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade
//   *     average
//   */
//  public Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
//    Iterable<Assignment> assignmentIterable = repository.findAll();
//    Set<Assignment> assignments =
//        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
//
//    return studentService.getAllStudents().stream()
//        .filter(
//            student ->
//                assignments.stream()
//                    .anyMatch(assignment -> assignment.getStudentId().equals(student.getId())))
//        .map(
//            student ->
//                new AbstractMap.SimpleEntry<Long, Double>(
//                    student.getId(),
//                    (double)
//                            assignments.stream()
//                                .filter(
//                                    assignment -> assignment.getStudentId().equals(student.getId()))
//                                .map(Assignment::getGrade)
//                                .reduce(0, Integer::sum)
//                        / (double)
//                            assignments.stream()
//                                .filter(
//                                    assignment -> assignment.getStudentId().equals(student.getId()))
//                                .count()))
//        .max((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue()));
//  }
//
//  /**
//   * Returns the id of the lab problem which was assigned the most often
//   *
//   * @return an {@code Optional} containing a null if no student is in the repository otherwise an
//   *     {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of
//   *     assignments
//   */
//  public Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {
//    Iterable<Assignment> assignmentIterable = repository.findAll();
//    Set<Assignment> assignments =
//        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
//
//    return labProblemService.getAllLabProblems().stream()
//        .map(
//            labProblem ->
//                new AbstractMap.SimpleEntry<Long, Long>(
//                    labProblem.getId(),
//                    assignments.stream()
//                        .filter(
//                            assignment -> assignment.getLabProblemId().equals(labProblem.getId()))
//                        .count()))
//        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())));
//  }
//
//  /**
//   * Returns the average grade of all the groups
//   *
//   * @return an {@code Optional} containing a null if no student is in the repository otherwise a
//   *     {@code Double} which represents the average grade
//   */
//  public Optional<Double> averageGrade() {
//    Iterable<Assignment> assignmentIterable = repository.findAll();
//    Set<Assignment> assignments =
//        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
//    int gradesSum = assignments.stream().map(Assignment::getGrade).reduce(0, Integer::sum);
//
//    if (assignments.size() > 0) {
//      return Optional.of((double) gradesSum / (double) assignments.size());
//    } else {
//      return Optional.empty();
//    }
//  }
//  /**
//   * Return a mapping of every Student and a list of it's assigned LabProblems.
//   *
//   * @return the sought Student - List of LabProblems. If student has no assignment, map to an empty
//   *     list.
//   */
//  public Optional<Map<Student, List<LabProblem>>> studentAssignedProblems() {
//
//    Map<Student, List<LabProblem>> result =
//        studentService.getAllStudents().stream()
//            .collect(
//                Collectors.toMap(
//                    student -> student, student -> getAllLabProblemsForAStudent(student)));
//    return Optional.ofNullable(result);
//    }
//
//
//  private List<LabProblem> getAllLabProblemsForAStudent(Student student) {
//    return StreamSupport.stream(repository.findAll().spliterator(), false)
//        .filter(assignment -> assignment.getStudentId().equals(student.getId()))
//        .map(assignment -> labProblemService.getLabProblemById(assignment.getLabProblemId()).get())
//        .collect(Collectors.toList());
//  }
}
