package domain.validators;

import domain.Student;
import domain.exceptions.ValidatorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentValidatorTest {

  private static final Long ID = 1L;
  private static final String SERIAL_NUMBER = "sn01";
  private static final String NAME = "studentName";
  private static final int GROUP = 123;

  Student student;
  StudentValidator validator;

  @BeforeEach
  void setUp() {
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
    validator = new StudentValidator();
  }

  @AfterEach
  void tearDown() {
    student = null;
    validator = null;
  }

  @Test
  void whenIdNull_throwException() {
    student.setId(null);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }

  @Test
  void whenSerialNumberNonAlphanumeric_throwException(){
    student.setSerialNumber("!");
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }

  @Test
  void whenSerialNumberEmpty_throwException(){
    student.setSerialNumber("");
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }

  @Test
  void whenNameEmpty_throwException(){
    student.setName("");
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }

  @Test
  void whenGroupNegative_throwException(){
    student.setGroup(-1);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }

  @Test
  void whenIdNegative_throwException(){
    student.setId(-1L);
    Assertions.assertThrows(ValidatorException.class, () -> {validator.validate(student);});
  }
}