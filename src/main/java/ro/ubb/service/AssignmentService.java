package ro.ubb.service;

import javafx.util.Pair;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.Repository;

import java.util.*;
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
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);
    assignmentValidator.validate(assignment);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      return repository.save(assignment);
    }
    return Optional.of(assignment);
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
    Assignment assignment = new Assignment(studentID, labProblemID, grade);
    assignment.setId(id);

    if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
      assignmentValidator.validate(assignment);
      return repository.update(assignment);
    }
    return Optional.of(assignment);
  }

  /**
   * Returns the student id who has the biggest mean of grades
   * @return an {@code Optional} containing a null if no student is in the repository otherwise
   *   an {@code Optional} containing a {@code Pair} of Long and Double, for the ID and the grade average
   */
  public Optional<Pair<Long, Double>> greatestMean(){
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments = StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    Double maximumMean = (double) -1;
    Long idOfStudentWithMaxMean = null;
    for(Student student : studentService.getAllStudents()){
      int gradesSum = assignments.stream()
          .filter(assignment -> assignment.getStudentId().equals(student.getId()))
          .map(Assignment::getGrade)
          .reduce(0, Integer::sum);
      long gradesCount = assignments.stream()
          .filter(assignment -> assignment.getStudentId().equals(student.getId()))
          .count();
      double studentMean = (double) gradesSum / (double) gradesCount;
      if(studentMean > maximumMean){
        maximumMean = studentMean;
        idOfStudentWithMaxMean = student.getId();
      }
    }
    if(idOfStudentWithMaxMean != null){
      return Optional.of(new Pair<>(idOfStudentWithMaxMean, maximumMean));
    } else {
      return Optional.empty();
    }

  }

  /**
   * Returns the id of the lab problem which was assigned the most often
   * @return an {@code Optional} containing a null if no student is in the repository otherwise
   *   an {@code Optional} containing a {@code Pair} of Long and Long, for the ID and the number of assignments
   */
  public Optional<Pair<Long, Long>> idOfLabProblemMostAssigned(){
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments = StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    Long idOfLabProblemMostAssigned = null;
    long maximumNumberOfAssignments = -1L;
    for(LabProblem labProblem : labProblemService.getAllLabProblems()){
      long count = assignments.stream()
          .filter(assignment -> assignment.getLabProblemId().equals(labProblem.getId()))
          .count();
      if(count > maximumNumberOfAssignments){
        idOfLabProblemMostAssigned = labProblem.getId();
        maximumNumberOfAssignments = count;
      }
    }
    if(idOfLabProblemMostAssigned != null){
      return Optional.of(new Pair<>(idOfLabProblemMostAssigned, maximumNumberOfAssignments));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the average grade of all the groups
   * @return an {@code Optional} containing a null if no student is in the repository otherwise
   *   a {@code Double} which represents the average grade
   */
  public Optional<Double> averageGrade(){
    Iterable<Assignment> assignmentIterable = repository.findAll();
    Set<Assignment> assignments = StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    int gradesSum = assignments.stream()
        .map(Assignment::getGrade)
        .reduce(0, Integer::sum);
    if(assignments.size() > 0){
      return Optional.of((double) gradesSum / (double) assignments.size());
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the group number with the greatest mean grade
   * @return an {@code Optional} containing a null if no student is in the repository otherwise
   *   an {@code Optional} containing a {@code Pair} of Integer and Double, for the group number and the average grade
   */
  public Optional<Pair<Integer, Double>> groupWithGreatestMean(){
    Map<Integer, List<Student>> groups = new HashMap<>();
    for(Student student : studentService.getAllStudents()){
      if(groups.containsKey(student.getGroup())){
        groups.get(student.getGroup()).add(student);
      } else {
        List<Student> tempStudentList = new ArrayList<>();
        tempStudentList.add(student);
        groups.put(student.getGroup(), tempStudentList);
      }
    }
    Map<Integer, Double> groupMeans = new HashMap<>();
    for (Map.Entry<Integer, List<Student>> entry : groups.entrySet()) {
      groupMeans.put(entry.getKey(), meanOfStudentsGrades(entry.getValue()));
    }
    Optional<Map.Entry<Integer, Double>> maximumMeanGroup = groupMeans.entrySet().stream().max(Map.Entry.comparingByValue());
    return maximumMeanGroup.map(entry -> new Pair<>(entry.getKey(), entry.getValue()));
  }


  private double meanOfStudentsGrades(Iterable<Student> students){
    long gradesCount = -1, gradesSum = 0;
    double meansSum = 0;
    double studentMean = 0;
    for (Student student : students) {
      gradesSum =
          getAllAssignments().stream()
              .filter(assignment -> assignment.getStudentId().equals(student.getId()))
              .map(Assignment::getGrade)
              .reduce(0, Integer::sum);
      gradesCount =
          getAllAssignments().stream()
              .filter(assignment -> assignment.getStudentId().equals(student.getId()))
              .count();
      studentMean = (double) gradesSum / (double) gradesCount;
      meansSum += studentMean;
      }
    if (students.spliterator().getExactSizeIfKnown() != 0) return (double) meansSum / (double) students.spliterator().getExactSizeIfKnown();
    else return 0;
  }

  }
