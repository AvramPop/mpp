package ro.ubb.domain.validators;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;

class AssignmentValidatorTest {

  private static final Long ID = 1L;
  private static final Long STUDENT_ID = 1L;
  private static final Long LAB_PROBLEM_ID = 1L;
  private static final int GRADE = 7;

  private Assignment assignment;
  private AssignmentValidator validator;

  @BeforeEach
  void setUp() {
    assignment = new Assignment(STUDENT_ID, LAB_PROBLEM_ID, GRADE);
    assignment.setId(ID);
    validator = new AssignmentValidator();
  }

  @AfterEach
  void tearDown() {
    assignment = null;
    validator = null;
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNullId_Then_ThrowsValidatorException() {
    assignment.setId(null);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNegativeId_Then_ThrowsValidatorException() {
    assignment.setId(-1L);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNullStudentId_Then_ThrowsValidatorException() {
    assignment.setStudentId(null);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNegativeStudentId_Then_ThrowsValidatorException() {
    assignment.setStudentId(-1L);
    Assertions.assertThrows(
        ValidatorException.class,
        () -> {
          validator.validate(assignment);
        });
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNullLabProblemId_Then_ThrowsValidatorException() {
    assignment.setLabProblemId(null);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithNegativeLabProblemId_Then_ThrowsValidatorException() {
    assignment.setLabProblemId(-1l);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }

  @Test
  void
      Given_AssignmentValidator_When_ValidatingAssignmentWithInvalidGrade_Then_ThrowsValidatorException() {
    assignment.setGrade(-1);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
    assignment.setGrade(11);
    Assertions.assertThrows(ValidatorException.class, () -> validator.validate(assignment));
  }
}
