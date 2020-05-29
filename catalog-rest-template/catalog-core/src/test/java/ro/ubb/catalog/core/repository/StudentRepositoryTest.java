package ro.ubb.catalog.core.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.ITConfig;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.StudentService;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data-student.xml")
public class StudentRepositoryTest {

  @Autowired
  private StudentRepository studentRepository;

  @Test
  public void findAll() throws Exception {
    List<Student> students = studentRepository.findAll();
    assertEquals("there should be four students", 4, students.size());
  }

  @Test
  @Transactional
  public void updateStudent() throws Exception {
    Student student = new Student("sn5", "name", 5);
    student.setId(1l);
    Student update = studentRepository.findById(1L).get();
    update.setSerialNumber(student.getSerialNumber());
    update.setName(student.getName());
    update.setGroupNumber(student.getGroupNumber());
    Student studentDB = studentRepository.findById(1l).get();
    assertEquals(studentDB.getSerialNumber(), student.getSerialNumber());
  }

  @Test
  public void createStudent() throws Exception {
    Student student = new Student("sn5", "name", 5);
    student.setId(5l);
    studentRepository.save(student);
    List<Student> students = studentRepository.findAll();
    assertEquals("there should be five students", 5, students.size());

  }

  @Test
  public void deleteStudent() throws Exception {
    Student student = new Student("sn1", "john", 1);
    student.setId(1l);
    studentRepository.delete(student);
    List<Student> students = studentRepository.findAll();
    assertEquals("there should be five students", 3, students.size());
  }

}
