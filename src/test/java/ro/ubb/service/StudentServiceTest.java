package ro.ubb.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.StudentValidator;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.inmemory.InMemoryRepository;

class StudentServiceTest {
  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String SERIAL_NUMBER = "sn01";
  private static final String NEW_SERIAL_NUMBER = "sn02";
  private static final String NAME = "studentName";
  private static final String NEW_NAME = "newStudentName";
  private static final int GROUP = 123;
  private static final int NEW_GROUP = 999;
  private Student student;
  private StudentService studentService;
  private SortingRepository<Long, Student> studentRepository;
  private Validator<Student> studentValidator;

  @BeforeEach
  void setUp() {
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
    studentValidator = new StudentValidator();
    studentRepository = new InMemoryRepository<>();
    studentService = new StudentService(studentRepository, studentValidator);
  }

  @AfterEach
  void tearDown() {
    studentService = null;
    student = null;
    studentRepository = null;
    studentValidator = null;
  }

  @Test
  void Given_EmptyRepository_When_ValidStudentAdded_Then_AdditionWillSucceed() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setId(-1L);
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.addStudent(-1L, SERIAL_NUMBER, NAME, GROUP));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidNameAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setName("");
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.addStudent(ID, SERIAL_NUMBER, "", GROUP));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidSerialNumberAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setSerialNumber("ad s");
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.addStudent(ID, "ad s", NAME, GROUP));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidGroupIDAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setGroup(-1);
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.addStudent(ID, SERIAL_NUMBER, NAME, -1));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_ReadingAllStudentEntitiesFormRepository_Then_NumberOfEntitiesReturnedIsOne() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertEquals(studentService.getAllStudents().size(), 1);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_NumberOfEntitiesReturnedIsZero() {

    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    studentService.deleteStudent(student.getId());
    Assertions.assertEquals(studentService.getAllStudents().size(), 0);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNotNull() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertTrue(studentService.deleteStudent(student.getId()).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalContainsTheEntity() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertEquals(studentService.deleteStudent(student.getId()).get(), student);
  }

  @Test
  void
      Given_EmptyRepository_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    Assertions.assertFalse(studentService.deleteStudent(student.getId()).isPresent());
  }

  @Test
  void Given_EmptyRepository_When_DeletingANull_Then_ThenThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> studentService.deleteStudent(null));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityNotInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertFalse(studentService.deleteStudent(NEW_ID).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_UpdateSucceedsReturnsEmptyOptional() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    Assertions.assertFalse(
        studentService.updateStudent(ID, NEW_SERIAL_NUMBER, NAME, GROUP).isPresent());
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_EntityIsUpdated() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    studentService.updateStudent(ID, NEW_SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertEquals(student, studentService.getAllStudents().toArray()[0]);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingNonExistingEntity_Then_UpdateFailsReturnsOptionalWithEntity() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    student.setId(2L);
    Assertions.assertEquals(
        studentService.updateStudent(2L, SERIAL_NUMBER, NAME, GROUP).get(), student);
  }

  @Test
  void Given_EmptyStudentRepository_When_FilteringByGroup_Then_ReturnsEmptySet() {
    Assertions.assertEquals(studentService.filterByGroup(GROUP).size(), 0);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FilteringByGroupWhichIsInRepository_Then_SetWithOneElement() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertEquals(studentService.filterByGroup(GROUP).size(), 1);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalContainingIt() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertEquals(studentService.getStudentById(student.getId()).get(), student);
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalWithValueInside() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertTrue(studentService.getStudentById(student.getId()).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingByIdOtherEntity_Then_ReturnOptionalWithNullInside() {
    studentService.addStudent(ID, SERIAL_NUMBER, NAME, GROUP);
    Assertions.assertFalse(studentService.getStudentById(2L).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingByIdWithInvalidId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> studentService.getStudentById(-1L));
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> studentService.getStudentById(null));
  }

  @Test
  void Given_StudentService_When_SavingInvalidEntity_Then_ThrowsValidatorException() {
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.addStudent(null, "", "", -1));
  }

  @Test
  void Given_StudentService_When_UpdatingInvalidEntity_Then_ThrowsValidatorException() {
    Assertions.assertThrows(
        ValidatorException.class, () -> studentService.updateStudent(null, "", "", -1));
  }
}
