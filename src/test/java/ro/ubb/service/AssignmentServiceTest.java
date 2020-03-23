package ro.ubb.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.AssignmentValidator;
import ro.ubb.domain.validators.LabProblemValidator;
import ro.ubb.domain.validators.StudentValidator;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.inmemory.InMemoryRepository;

class AssignmentServiceTest {

  private static final Long IDLABPROBLEM = 1L;
  private static final Long NEW_IDLABPROBLEM = 2L;
  private static final String DESCRIPTIONLABPROBLEM = "123123";
  private static final String NEW_DESCRIPTIONLABPROBLEM = "123";
  private static final int PROBLEM_NUMBERLABPROBLEM = 123;
  private static final int NEW_PROBLEM_NUMBERLABPROBLEM = 999;
  private LabProblem labProblem;
  private LabProblemService labProblemService;
  private Validator<LabProblem> labProblemValidator;
  private SortingRepository<Long, LabProblem> labProblemRepository;

  private static final Long IDSTUDENT = 1L;
  private static final Long NEW_IDSTUDENT = 2L;
  private static final String SERIAL_NUMBERSTUDENT = "sn01";
  private static final String NEW_SERIAL_NUMBERSTUDENT = "sn02";
  private static final String NAMESTUDENT = "studentName";
  private static final String NEW_NAMESTUDENT = "newStudentName";
  private static final int GROUPSTUDENT = 123;
  private static final int NEW_GROUPSTUDENT = 999;
  private Student student;
  private StudentService studentService;
  private SortingRepository<Long, Student> studentRepository;
  private Validator<Student> studentValidator;

  private static final Long IDASSIGNMENT = 1L;
  private static final Long NEW_IDASSIGNMENT = 2L;
  private static final int GRADE = 10;
  private Assignment assignment;
  private AssignmentService assignmentService;
  private SortingRepository<Long, Assignment> assignmentRepository;
  private Validator<Assignment> assignmentValidator;

  @BeforeEach
  void setUp() {
    labProblem = new LabProblem(PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    labProblem.setId(IDLABPROBLEM);
    labProblemValidator = new LabProblemValidator();
    labProblemRepository = new InMemoryRepository<>();
    labProblemService = new LabProblemService(labProblemRepository, labProblemValidator);

    student = new Student(SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    student.setId(IDSTUDENT);
    studentValidator = new StudentValidator();
    studentRepository = new InMemoryRepository<>();
    studentService = new StudentService(studentRepository, studentValidator);

    assignment = new Assignment(student.getId(), labProblem.getId());
    assignment.setId(IDASSIGNMENT);
    assignmentRepository = new InMemoryRepository<>();
    assignmentValidator = new AssignmentValidator();
    assignmentService =
        new AssignmentService(
            assignmentRepository, labProblemService, studentService, assignmentValidator);
  }

  @AfterEach
  void tearDown() {
    assignment = null;
    assignmentRepository = null;
    assignmentService = null;

    labProblemService = null;
    labProblem = null;
    labProblemRepository = null;
    labProblemValidator = null;

    studentService = null;
    student = null;
    studentRepository = null;
    studentValidator = null;
  }

  @Test
  void Given_EmptyRepository_When_ValidAssignmentAdded_Then_AdditionWillSucceed() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfAssignmentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);

    Assertions.assertThrows(
        ValidatorException.class,
        () -> assignmentService.addAssignment(-1L, IDSTUDENT, IDLABPROBLEM, GRADE));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidStudentIdOfAssignmentEntity_Then_AdditionWillFailAndThrowsValidatorExcepton() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);

    Assertions.assertThrows(
        ValidatorException.class,
        () -> assignmentService.addAssignment(IDASSIGNMENT, -1L, IDLABPROBLEM, GRADE));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidLabProblemIdOfAssignmentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);

    Assertions.assertThrows(
        ValidatorException.class,
        () -> assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, -1L, GRADE));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidGradeSmallerThanZeroOfAssignmentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);

    Assertions.assertThrows(
        ValidatorException.class,
        () -> assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, -1));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidGradeLargerThanZeroOfAssignmentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);

    Assertions.assertThrows(
        ValidatorException.class,
        () -> assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, 11));
  }

  @Test
  void
      Given_EmptyRepository_When_ValidEntityStudentIdNotInRepositoryOfAssignmentEntity_Then_AdditionWillFailReturnsEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    Assertions.assertTrue(
        assignmentService
            .addAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE)
            .isPresent());
  }

  @Test
  void
      Given_EmptyRepository_When_ValidEntityLabPRoblemIdNotInRepositoryOfAssignmentEntity_Then_AdditionWillFailReturnsEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    Assertions.assertTrue(
        assignmentService
            .addAssignment(IDASSIGNMENT, IDSTUDENT, NEW_IDLABPROBLEM, GRADE)
            .isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_ReadingAllStudentEntitiesFormRepository_Then_NumberOfEntitiesReturnedIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 1);
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_NumberOfEntitiesReturnedIsZero() {

    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteAssignment(assignment.getId());
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 0);
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalContainingIt() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertEquals(
        assignmentService.getAssignmentById(assignment.getId()).get(), assignment);
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalWithValueInside() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertTrue(assignmentService.getAssignmentById(assignment.getId()).isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingByIdOtherEntity_Then_ReturnOptionalWithNullInside() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertFalse(assignmentService.getAssignmentById(2L).isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNotNull() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertTrue(assignmentService.deleteAssignment(assignment.getId()).isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalContainsTheEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertEquals(
        assignmentService.deleteAssignment(assignment.getId()).get(), assignment);
  }

  @Test
  void
      Given_EmptyRepository_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    Assertions.assertFalse(assignmentService.deleteAssignment(assignment.getId()).isPresent());
  }

  @Test
  void Given_EmptyRepository_When_DeletingANull_Then_ThenThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentService.deleteAssignment(null));
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingAnEntityNotInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertFalse(assignmentService.deleteAssignment(NEW_IDASSIGNMENT).isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_UpdateSucceedsReturnsEmptyOptional() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    studentService.addStudent(NEW_IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    Assertions.assertFalse(
        assignmentService
            .updateAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE)
            .isPresent());
  }

  @Test
  void Given_AssignmentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_EntityIsUpdated() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.updateAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE);
    assignment.setStudentId(NEW_IDSTUDENT);

    Assertions.assertEquals(assignment, assignmentService.getAllAssignments().toArray()[0]);
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_UpdatingNonExistingEntity_Then_UpdateFailsReturnsOptionalWithEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.updateAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE);
    assignment.setStudentId(NEW_IDSTUDENT);
    Assertions.assertEquals(
        assignmentService.updateAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE).get(),
        assignment);
  }

  @Test
  void
      Given_EmptyRepository_When_ValidEntityStudentIdNotInRepositoryOfUpdateEntity_Then_UpdateWillFailReturnsEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(
        assignmentService
            .updateAssignment(IDASSIGNMENT, NEW_IDSTUDENT, IDLABPROBLEM, GRADE)
            .isPresent());
  }

  @Test
  void
      Given_EmptyRepository_When_ValidEntityLabPRoblemIdNotInRepositoryOfUpdateEntity_Then_UpdateWillFailReturnsEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(
        assignmentService
            .updateAssignment(IDASSIGNMENT, IDSTUDENT, NEW_IDLABPROBLEM, GRADE)
            .isPresent());
  }

  @Test
  void Given_EmptyRepository_When_GettingGreatestMean_Then_NullIsReturned() {
    Assertions.assertTrue(assignmentService.greatestMean().isEmpty());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_GettingGreatestMean_Then_EntityIsReturned() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(assignmentService.greatestMean().isPresent());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_GettingGreatestMean_Then_MeanIsTheEntityGrade() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.greatestMean().get().getValue(), GRADE);
  }

  @Test
  void Given_RepositoryWithOneEntity_When_GettingGreatestMean_Then_MeanIDIsTheEntityGrade() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.greatestMean().get().getKey(), IDLABPROBLEM);
  }

  @Test
  void Given_EmptyRepository_When_IdOfMostAssignedLabProblem_Then_NullIsReturned() {
    Assertions.assertTrue(assignmentService.idOfLabProblemMostAssigned().isEmpty());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_IdOfMostAssignedLabProblem_Then_EntityIsReturned() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(assignmentService.idOfLabProblemMostAssigned().isPresent());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_IdOfMostAssignedLabProblem_Then_EntityIDIsTheEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(
        assignmentService.idOfLabProblemMostAssigned().get().getKey(), IDLABPROBLEM);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_IdOfMostAssignedLabProblem_Then_TheNumberOfAssignmentsIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.idOfLabProblemMostAssigned().get().getValue(), 1);
  }

  @Test
  void Given_EmptyRepository_When_GettingAverageGrade_Then_NullIsReturned() {
    Assertions.assertTrue(assignmentService.averageGrade().isEmpty());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_GettingAverageGrade_Then_EntityIsReturned() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(assignmentService.averageGrade().isPresent());
  }

  @Test
  void Given_RepositoryWithOneEntity_When_GettingAverageGrade_Then_GradeIsTheEntityGrade() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.averageGrade().get(), GRADE);
  }

  /*
    @Test
    void Given_EmptyRepository_When_GroupWithGreatestMean_Then_NullIsReturned() {
      Assertions.assertTrue(assignmentService.groupWithGreatestMean().isEmpty());
    }

    @Test
    void Given_RepositoryWithOneEntity_When_GroupWithGreatestMean_Then_EntityIsReturned() {
      labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
      studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
      assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

      Assertions.assertTrue(assignmentService.groupWithGreatestMean().isPresent());
    }

    @Test
    void Given_RepositoryWithOneEntity_When_GroupWithGreatestMean_Then_GroupIsEntityGroup() {
      labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
      studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
      assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

      Assertions.assertEquals(assignmentService.groupWithGreatestMean().get().getKey(), GROUPSTUDENT);
    }

    @Test
    void Given_RepositoryWithOneEntity_When_GroupWithGreatestMean_Then_TheNumberOfAssignmentsIsOne() {
      labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
      studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
      assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

      Assertions.assertEquals(assignmentService.groupWithGreatestMean().get().getValue(), 10);
    }
  */
  /*

  */

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingStudentCorrespondingToThatTransaction_Then_DeleteSucceeds() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(assignmentService.deleteStudent(IDSTUDENT).isPresent());
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingStudentCorrespondingToThatTransaction_Then_DeleteReturnsCorrectEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.deleteStudent(IDSTUDENT).get(), student);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingStudentCorrespondingToThatTransaction_Then_NumberOfAssignmentsIsZero() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteStudent(IDSTUDENT);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 0);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingStudentNotInRepository_Then_NumberOfAssignmentsIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteStudent(NEW_IDSTUDENT);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 1);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingStudentInRepositoryWhichIsNotAssigned_Then_NumberOfAssignmentsIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    labProblemService.addLabProblem(
        NEW_IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteStudent(NEW_IDSTUDENT);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 1);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingInvalidStudentID_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentService.deleteStudent(-1L));
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingInvalidStudentWithNullValue_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentService.deleteStudent(null));
  }
  /*


  */
  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingLabProblemCorrespondingToThatTransaction_Then_DeleteSucceeds() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertTrue(assignmentService.deleteLabProblem(IDLABPROBLEM).isPresent());
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingLabProblemCorrespondingToThatTransaction_Then_DeleteReturnsCorrectEntity() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);

    Assertions.assertEquals(assignmentService.deleteLabProblem(IDLABPROBLEM).get(), labProblem);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingLabProblemCorrespondingToThatTransaction_Then_NumberOfAssignmentsIsZero() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteLabProblem(IDLABPROBLEM);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 0);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingLabProblemNotInRepository_Then_NumberOfAssignmentsIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteLabProblem(NEW_IDLABPROBLEM);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 1);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingLabProblemInRepositoryWhichIsNotAssigned_Then_NumberOfAssignmentsIsOne() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    labProblemService.addLabProblem(
        NEW_IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.deleteLabProblem(NEW_IDLABPROBLEM);
    Assertions.assertEquals(assignmentService.getAllAssignments().size(), 1);
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingInvalidLabProblemID_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentService.deleteLabProblem(-1L));
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_DeletingInvalidLabProblemWithNullValue_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentService.deleteLabProblem(null));
  }

  @Test
  void
      Given_RepositoryWithOneEntity_When_GeneratinReportForAllEntites_Then_TheStudentHasProblemsAssignedToItEmptyProblemsAreDisregarded() {
    labProblemService.addLabProblem(IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    labProblemService.addLabProblem(
        NEW_IDLABPROBLEM, PROBLEM_NUMBERLABPROBLEM, DESCRIPTIONLABPROBLEM);
    studentService.addStudent(IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    studentService.addStudent(NEW_IDSTUDENT, SERIAL_NUMBERSTUDENT, NAMESTUDENT, GROUPSTUDENT);
    assignmentService.addAssignment(IDASSIGNMENT, IDSTUDENT, IDLABPROBLEM, GRADE);
    assignmentService.addAssignment(NEW_IDASSIGNMENT, NEW_IDSTUDENT, NEW_IDASSIGNMENT, GRADE);
    Assertions.assertEquals(assignmentService.studentAssignedProblems().get().size(), 2);
  }
}
