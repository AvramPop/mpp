package domain.validators;

import domain.LabProblem;
import domain.exceptions.ValidatorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LabProblemValidatorTest {

  private static final Long ID = 1L;
  private static final String DESCRIPTION = "description";
  private static final int PROBLEM_NUMBER = 123;

  LabProblem labProblem;
  LabProblemValidator validator;

  @BeforeEach
  void setUp() {
    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
    validator = new LabProblemValidator();
  }

  @AfterEach
  void tearDown() {
    labProblem = null;
    validator = null;
  }

  @Test
  void whenIdNull_throwException() {
    labProblem.setId(null);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(labProblem);});
  }

  @Test
  void whenIdNegative_throwException(){
    labProblem.setId(-1L);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(labProblem);});
  }

  @Test
  void whenDescriptionEmpty_throwException() {
    labProblem.setDescription("");
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(labProblem);});
  }

  @Test
  void whenLabProblemNumberIsNegative_throwException() {
    labProblem.setProblemNumber(-1);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(labProblem);});
  }


}