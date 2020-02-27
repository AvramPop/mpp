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

import static org.junit.jupiter.api.Assertions.*;

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
    student = new Student(SERIAL_NUMBER,NAME,GROUP);
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
  void addStudentSuccess() {
    studentService.addStudent(student);
  }

  @Test
  void addStudentFailsInvalidID() {
    student.setId(-1L);
    Assertions.assertThrows(ValidatorException.class, ()->studentService.addStudent(student));

  }
  @Test
  void addStudentFailsInvalidName() {
    student.setName("");
    Assertions.assertThrows(ValidatorException.class, ()->studentService.addStudent(student));

  }
  @Test
  void addStudentFailsInvalidSerial() {
    student.setSerialNumber("ad s");
    Assertions.assertThrows(ValidatorException.class, ()->studentService.addStudent(student));

  }
  @Test
  void addStudentFailsInvalidGroup() {
    student.setGroup(-1);
    Assertions.assertThrows(ValidatorException.class, ()->studentService.addStudent(student));

  }

  @Test
  void getAllStudents() {
    studentService.addStudent(student);
    Assertions.assertEquals(studentService.getAllStudents().size(),1);
  }
}