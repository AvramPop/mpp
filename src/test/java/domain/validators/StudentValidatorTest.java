package domain.validators;

import domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentValidatorTest {

  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String SERIAL_NUMBER = "sn01";
  private static final String NEW_SERIAL_NUMBER = "sn02";
  private static final String NAME = "studentName";
  private static final int GROUP = 123;

  Student student;
  StudentValidator validator;

  @BeforeEach
  void setUp() throws Exception{
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
    validator = new StudentValidator();
  }

  @AfterEach
  void tearDown() {
    student = null;
  }

  @Test
  void validate() {}
}