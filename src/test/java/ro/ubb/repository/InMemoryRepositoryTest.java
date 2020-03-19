package ro.ubb.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Student;
import ro.ubb.repository.inmemory.InMemoryRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class InMemoryRepositoryTest {

  private Repository<Long, Student> repository;
  private Student student;

  @BeforeEach
  void setUp() {
    repository = new InMemoryRepository<>();
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
  void
      Given_StudentRepositoryWithOneEntity_When_FindingSerialNumberOfOneThatIsInRepository_Then_ReturnSoughtSerialNumber() {
    Assertions.assertEquals("sn1", repository.findOne(1L).get().getSerialNumber());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingOneThatIsNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(repository.findOne(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_FindingOneThatIsInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(repository.findOne(1L).isPresent(), "member with id given found");
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_WhenFindOneWithNullId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findOne(null));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_FindingAllInRepository_Then_ReturnSetWithThatEntity() {
    Set<Student> students = new HashSet<>();
    students.add(student);
    Assertions.assertEquals(students, repository.findAll());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_SavingNewValidEntity_Then_NumberOfEntitiesInRepositoryIncrements() {
    Student student2 = new Student("sn2", "studentName2", 2);
    student2.setId(2L);
    repository.save(student2);
    Assertions.assertEquals(2, ((Set<Student>) repository.findAll()).size());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_SavingEntityWithIdAlreadyInRepository_Then_SizeOfRepositoryRemainsOne() {
    repository.save(student);
    Assertions.assertEquals(1, ((Set<Student>) repository.findAll()).size());
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_SavingNull_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(null));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingSingleEntityInRepository_Then_SizeOfRepositoryIsZero() {
    repository.delete(1L);
    Assertions.assertEquals(0, ((Set<Student>) repository.findAll()).size());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnRightDataAboutTheDeletedEntity() {
    Assertions.assertEquals("sn1", repository.delete(1L).get().getSerialNumber());
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_DeletingEntityNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(repository.delete(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(repository.delete(1L).isPresent(), "member with id given deleted");
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_DeletingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingEntityNotInRepository_Then_ReturnOptionalWithEntity() {
    Student updatedStudent2 = new Student("sn2", "updatedStudentName2", 2);
    updatedStudent2.setId(2L);
    Optional<Student> updateResult = repository.update(updatedStudent2);
    Assertions.assertTrue(updateResult.isPresent());
    Assertions.assertEquals(updatedStudent2, updateResult.get());
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_UpdateEntity() {
    Student updatedStudent = new Student("sn1", "updatedStudentName", 1);
    updatedStudent.setId(1L);
    repository.update(updatedStudent);
    Assertions.assertEquals("updatedStudentName", repository.findOne(1L).get().getName());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_ReturnOptionalWithNoValue() {
    Student updatedStudent = new Student("sn1", "updatedStudentName", 1);
    updatedStudent.setId(1L);
    Assertions.assertFalse(repository.update(updatedStudent).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.update(null));
  }
}
