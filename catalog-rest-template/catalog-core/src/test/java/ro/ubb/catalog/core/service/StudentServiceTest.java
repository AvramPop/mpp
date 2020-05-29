package ro.ubb.catalog.core.service;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import ro.ubb.catalog.core.ITConfig;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.StudentService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data-student.xml")
public class StudentServiceTest {

  @Autowired
  private StudentService studentService;

  @Test
  public void findAll() throws Exception {
    List<Student> students = studentService.getAllStudents();
    assertEquals("there should be four students", 4, students.size());
  }

  @Test
  public void updateStudent() throws Exception {
    Student student = new Student("sn5", "name", 5);
    student.setId(1l);
    studentService.updateStudent(1l, student);
    Student studentDB = studentService.getStudentById(1l);
    assertEquals(studentDB.getSerialNumber(), student.getSerialNumber());
  }

  @Test
  public void createStudent() throws Exception {
    Student student = new Student("sn5", "name", 5);
    student.setId(5l);
    studentService.saveStudent(student);
    List<Student> students = studentService.getAllStudents();
    assertEquals("there should be five students", 5, students.size());

  }

  @Test
  public void deleteStudent() throws Exception {
    studentService.deleteStudent(1L);
    List<Student> students = studentService.getAllStudents();
    assertEquals("there should be five students", 3, students.size());
  }

}