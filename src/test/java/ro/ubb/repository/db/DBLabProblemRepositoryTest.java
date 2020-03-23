package ro.ubb.repository.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.LabProblem;
import ro.ubb.repository.Repository;

import java.nio.file.FileSystems;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class DBLabProblemRepositoryTest {
  private Repository<Long, LabProblem> labProblemRepository;
  private LabProblem labProblem;

  @BeforeEach
  void setUp() {
    labProblemRepository =
        new DBLabProblemRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data",
            "public.\"LabProblems_test\"");
    labProblem = new LabProblem(11, "description");
    labProblem.setId(1L);
    labProblemRepository.save(labProblem);
  }

  @AfterEach
  void tearDown() {
    if (labProblemRepository.findOne(1L).isPresent()) labProblemRepository.delete(1L);
    labProblemRepository = null;
    labProblem = null;
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingDescriptionOfOneThatIsInRepository_Then_ReturnSoughtDescription() {
    Assertions.assertEquals("description", labProblemRepository.findOne(1L).get().getDescription());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingOneThatIsNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(
        labProblemRepository.findOne(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_FindingOneThatIsInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(
        labProblemRepository.findOne(1L).isPresent(), "member with id given found");
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_WhenFindOneWithNullId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemRepository.findOne(null));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingAllInRepository_Then_ReturnSetWithThatEntity() {
    Set<LabProblem> labProblems = new HashSet<>();
    labProblems.add(labProblem);
    Assertions.assertEquals(labProblems, labProblemRepository.findAll());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_SavingValidEntity_Then_NumberOfElementsInTheRepositoryIncrements() {
    LabProblem labProblem = new LabProblem(12, "description2");
    labProblem.setId(2L);
    labProblemRepository.save(labProblem);
    Assertions.assertEquals(2, ((Set<LabProblem>) labProblemRepository.findAll()).size());
    labProblemRepository.delete(2L);
  }

  @Test
  void
      Given_LabProblemWithOneEntity_When_SavingEntityWithIdAlreadyInRepository_Then_SizeOfRepositoryRemainsOne() {
    labProblemRepository.save(labProblem);
    Assertions.assertEquals(1, ((Set<LabProblem>) labProblemRepository.findAll()).size());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_SavingNull_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> labProblemRepository.save(null));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingSingleEntityInRepository_Then_SizeOfRepositoryIsZero() {
    labProblemRepository.delete(1L);
    Assertions.assertEquals(0, ((Set<LabProblem>) labProblemRepository.findAll()).size());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnRightDataAboutTheDeletedEntity() {
    Assertions.assertEquals("description", labProblemRepository.delete(1L).get().getDescription());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(
        labProblemRepository.delete(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(
        labProblemRepository.delete(1L).isPresent(), "member with id given deleted");
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemRepository.delete(null));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingEntityNotInRepository_Then_ReturnOptionalWithEntity() {
    LabProblem updatedLabProblem2 = new LabProblem(12, "description2");
    updatedLabProblem2.setId(2L);
    Optional<LabProblem> updateResult = labProblemRepository.update(updatedLabProblem2);
    Assertions.assertTrue(updateResult.isPresent());
    Assertions.assertEquals(updatedLabProblem2, updateResult.get());
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_UpdateEntity() {
    LabProblem labProblem = new LabProblem(2, "descriptionUpdated");
    labProblem.setId(1L);
    labProblemRepository.update(labProblem);
    Assertions.assertEquals(
        "descriptionUpdated", labProblemRepository.findOne(1L).get().getDescription());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_ReturnOptionalWithNoValue() {
    LabProblem labProblem = new LabProblem(2, "descriptionUpdated");
    labProblem.setId(1L);
    Assertions.assertFalse(labProblemRepository.update(labProblem).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemRepository.update(null));
  }
}
