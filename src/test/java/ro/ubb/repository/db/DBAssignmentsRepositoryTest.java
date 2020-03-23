package ro.ubb.repository.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.repository.Repository;

import java.nio.file.FileSystems;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class DBAssignmentsRepositoryTest {
  private Repository<Long, Assignment> assignmentRepository;
  private Assignment assignment;
  private Repository<Long, LabProblem> labProblemRepository;
  private LabProblem labProblem;
  private Repository<Long, Student> studentRepository;
  private Student student;

  @BeforeEach
  void setUp() {

    labProblemRepository =
        new DBLabProblemRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data",
            "public.\"LabProblems_test\"");
    labProblem = new LabProblem(11, "description");
    labProblem.setId(1L);
    labProblemRepository.save(labProblem);

    studentRepository =
        new DBStudentRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data",
            "public.\"Students_test\"");
    student = new Student("sn1", "studentName", 1);
    student.setId(1L);
    studentRepository.save(student);

    assignmentRepository =
        new DBAssignmentsRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data",
            "public.\"Assignments_test\"");
    assignment = new Assignment(1L, 1L, 10);
    assignment.setId(1L);
    assignmentRepository.save(assignment);
  }

  @AfterEach
  void tearDown() {
    if (assignmentRepository.findOne(1L).isPresent()) assignmentRepository.delete(1L);
    assignmentRepository = null;
    assignment = null;

    if (labProblemRepository.findOne(1L).isPresent()) labProblemRepository.delete(1L);
    labProblemRepository = null;
    labProblem = null;

    if (studentRepository.findOne(1L).isPresent()) studentRepository.delete(1L);
    student = null;
    studentRepository = null;
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingDescriptionOfOneThatIsInRepository_Then_ReturnSoughtDescription() {
    Assertions.assertEquals(10, assignmentRepository.findOne(1L).get().getGrade());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingOneThatIsNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(
        assignmentRepository.findOne(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_AssignmentRepositoryWithOneEntity_When_FindingOneThatIsInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(
        assignmentRepository.findOne(1L).isPresent(), "member with id given found");
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_WhenFindOneWithNullId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentRepository.findOne(null));
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_FindingAllInRepository_Then_ReturnSetWithThatEntity() {
    Set<Assignment> assignments = new HashSet<>();
    assignments.add(assignment);
    Assertions.assertEquals(assignments, assignmentRepository.findAll());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_SavingValidEntity_Then_NumberOfElementsInTheRepositoryIncrements() {
    LabProblem labProblem = new LabProblem(12, "description2");
    labProblem.setId(2L);
    labProblemRepository.save(labProblem);
    Student student2 = new Student("sn2", "studentName2", 2);
    student2.setId(2L);
    studentRepository.save(student2);
    Assignment assignment = new Assignment(2L, 2L, 10);
    assignment.setId(2L);
    assignmentRepository.save(assignment);

    Assertions.assertEquals(2, ((Set<Assignment>) assignmentRepository.findAll()).size());
    assignmentRepository.delete(2L);
    labProblemRepository.delete(2L);
    studentRepository.delete(2L);
  }

  @Test
  void
      Given_AssignmentWithOneEntity_When_SavingEntityWithIdAlreadyInRepository_Then_SizeOfRepositoryRemainsOne() {
    assignmentRepository.save(assignment);
    Assertions.assertEquals(1, ((Set<Assignment>) assignmentRepository.findAll()).size());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_SavingNull_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> assignmentRepository.save(null));
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingSingleEntityInRepository_Then_SizeOfRepositoryIsZero() {
    assignmentRepository.delete(1L);
    Assertions.assertEquals(0, ((Set<Assignment>) assignmentRepository.findAll()).size());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnRightDataAboutTheDeletedEntity() {
    Assertions.assertEquals(10, assignmentRepository.delete(1L).get().getGrade());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingEntityNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(
        assignmentRepository.delete(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_AssignmentRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(
        assignmentRepository.delete(1L).isPresent(), "member with id given deleted");
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_DeletingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentRepository.delete(null));
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_UpdatingEntityNotInRepository_Then_ReturnOptionalWithEntity() {
    Assignment updatedAssignment2 = new Assignment(2L, 2L, 10);
    updatedAssignment2.setId(2L);
    Optional<Assignment> updateResult = assignmentRepository.update(updatedAssignment2);
    Assertions.assertTrue(updateResult.isPresent());
    Assertions.assertEquals(updatedAssignment2, updateResult.get());
  }

  @Test
  void Given_AssignmentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_UpdateEntity() {
    Assignment assignment = new Assignment(1L, 1L, 5);
    assignment.setId(1L);
    assignmentRepository.update(assignment);
    Assertions.assertEquals(5, assignmentRepository.findOne(1L).get().getGrade());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_ReturnOptionalWithNoValue() {
    Assignment assignment = new Assignment(1L, 1L, 5);
    assignment.setId(1L);
    Assertions.assertFalse(assignmentRepository.update(assignment).isPresent());
  }

  @Test
  void
      Given_AssignmentRepositoryWithOneEntity_When_UpdatingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> assignmentRepository.update(null));
  }
}
