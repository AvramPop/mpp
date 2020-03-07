package service;

import domain.Student;
import domain.exceptions.ValidatorException;
import domain.validators.StudentValidator;
import domain.validators.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;
import repository.Repository;

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
  private Repository<Long, Student> studentRepository;
  private Validator<Student> studentValidator;

  @BeforeEach
  void setUp() {
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
    studentValidator = new StudentValidator();
    studentRepository = new InMemoryRepository<>(studentValidator);
    studentService = new StudentService(studentRepository);
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
    studentService.addStudent(student);
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setId(-1L);
    Assertions.assertThrows(ValidatorException.class, () -> studentService.addStudent(student));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidNameAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setName("");
    Assertions.assertThrows(ValidatorException.class, () -> studentService.addStudent(student));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidSerialNumberAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setSerialNumber("ad s");
    Assertions.assertThrows(ValidatorException.class, () -> studentService.addStudent(student));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidGroupIDAttributeOfStudentEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    student.setGroup(-1);
    Assertions.assertThrows(ValidatorException.class, () -> studentService.addStudent(student));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_ReadingAllStudentEntitiesFormRepository_Then_NumberOfEntitiesReturnedIsOne() {
    studentService.addStudent(student);
    Assertions.assertEquals(studentService.getAllStudents().size(), 1);
  }
  
  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_NumberOfEntitiesReturnedIsZero() {

    studentService.addStudent(student);
    studentService.deleteStudent(student.getId());
    Assertions.assertEquals(studentService.getAllStudents().size(), 0);
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNotNull() {
    studentService.addStudent(student);
    Assertions.assertTrue(studentService.deleteStudent(student.getId()).isPresent());
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalContainsTheEntity() {
    studentService.addStudent(student);
    Assertions.assertEquals(
        studentService.deleteStudent(student.getId()).get(), student);
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
    studentService.addStudent(student);
    Assertions.assertFalse(studentService.deleteStudent(NEW_ID).isPresent());
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_UpdateSucceedsReturnsEmptyOptional() {
    studentService.addStudent(student);
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    Assertions.assertFalse(studentService.updateStudent(student).isPresent());
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_UpdatingTheEntity_Then_EntityIsUpdated() {
    studentService.addStudent(student);
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    studentService.updateStudent(student);
    Assertions.assertEquals(student, studentService.getAllStudents().toArray()[0]);
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_UpdatingNonExistingEntity_Then_UpdateFailsReturnsOptionalWithEntity() {
    studentService.addStudent(student);
    student.setId(2L);
    Assertions.assertEquals(studentService.updateStudent(student).get(), student);
  }

  @Test
  void Given_EmptyStudentRepository_When_FilteringByGroup_Then_ReturnsEmptySet() {
    Assertions.assertEquals(studentService.filterByGroup(GROUP).size(), 0);
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_FilteringByGroupWhichIsInRepository_Then_SetWithOneElement() {
    studentService.addStudent(student);
    Assertions.assertEquals(studentService.filterByGroup(GROUP).size(), 1);
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalContainingIt() {
    studentService.addStudent(student);
    Assertions.assertEquals(studentService.getStudentById(student.getId()).get(), student);
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalWithValueInside() {
    studentService.addStudent(student);
    Assertions.assertTrue(studentService.getStudentById(student.getId()).isPresent());
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_FindingByIdOtherEntity_Then_ReturnOptionalWithNullInside() {
    studentService.addStudent(student);
    Assertions.assertFalse(studentService.getStudentById(2L).isPresent());
  }

  @Test
  void
  Given_StudentRepositoryWithOneEntity_When_FindingByIdWithInvalidId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(-1l));
    Assertions.assertThrows(IllegalArgumentException.class, () -> studentService.getStudentById(null));
  }
}
