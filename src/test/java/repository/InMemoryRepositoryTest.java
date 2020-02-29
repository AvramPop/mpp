package repository;

import domain.Student;
import domain.exceptions.ValidatorException;
import domain.validators.StudentValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class InMemoryRepositoryTest {

  private Repository<Long, Student> repository;
  private Student student;

  @BeforeEach
  void setUp() {
    repository = new InMemoryRepository<>(new StudentValidator());
    student = new Student("sn1", "studentName", 1);
    student.setId(1L);
    repository.save(student);
  }

  @AfterEach
  void tearDown() {
    repository = null;
    student = null;
  }

  @Test
  void findOne() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findOne(null));
    Assertions.assertTrue(repository.findOne(1L).isPresent(), "member with id given found");
    Assertions.assertFalse(repository.findOne(2L).isPresent(), "member with id given not found");
    Assertions.assertEquals("sn1", repository.findOne(1L).get().getSerialNumber());
  }

  @Test
  void findAll() {
    Set<Student> students = new HashSet<>();
    students.add(student);
    Assertions.assertEquals(students, repository.findAll());
  }

  @Test
  void save() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    Assertions.assertThrows(ValidatorException.class, () -> repository.save(new Student()));
    repository.save(student);
    Assertions.assertEquals(1, ((Set<Student>) repository.findAll()).size());
    Student student2 = new Student("sn2", "studentName2", 2);
    student2.setId(2L);
    repository.save(student2);
    Assertions.assertEquals(2, ((Set<Student>) repository.findAll()).size());
  }

  @Test
  void delete() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
    Assertions.assertTrue(repository.delete(1L).isPresent(), "member with id given deleted");
    Assertions.assertFalse(repository.delete(2L).isPresent(), "member with id given not found");
    repository.save(student);
    Assertions.assertEquals("sn1", repository.delete(1L).get().getSerialNumber());
    Assertions.assertEquals(0, ((Set<Student>) repository.findAll()).size());
  }

  @Test
  void update() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.update(null));
    Assertions.assertThrows(ValidatorException.class, () -> repository.update(new Student()));
    Student updatedStudent = new Student("sn1", "updatedStudentName", 1);
    updatedStudent.setId(1L);
    Student updatedStudent2 = new Student("sn2", "updatedStudentName2", 2);
    updatedStudent2.setId(2L);
    Assertions.assertTrue(repository.update(updatedStudent).isPresent());
    Assertions.assertEquals("updatedStudentName", repository.findOne(1L).get().getName());
    Assertions.assertFalse(repository.update(updatedStudent2).isPresent());

  }
}