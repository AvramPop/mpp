package ro.ubb.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.LabProblem;
import ro.ubb.repository.file.FileLineEntityFactory;
import ro.ubb.repository.file.FileRepository;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class FileRepositoryTest {

  private Repository<Long, LabProblem> repository;
  private LabProblem labProblem;

  @BeforeEach
  void setUp() {
    try {
      Files.createFile(Paths.get(repoPath()));
    } catch (IOException ignored) {
    }
    labProblem = new LabProblem(11, "description");
    labProblem.setId(1L);
    repository =
        new FileRepository<>(repoPath(), ";", FileLineEntityFactory.labProblemFromFileLine());
    repository.save(labProblem);
  }

  @AfterEach
  void tearDown() {
    repository = null;
    labProblem = null;
    try {
      Files.delete(Paths.get(repoPath()));
    } catch (IOException ex) {

    }
  }

  private String repoPath() {
    return "src"
        + FileSystems.getDefault().getSeparator()
        + "test"
        + FileSystems.getDefault().getSeparator()
        + "resources"
        + FileSystems.getDefault().getSeparator()
        + "test.txt"
        + FileSystems.getDefault().getSeparator();
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingDescriptionOfOneThatIsInRepository_Then_ReturnSoughtDescription() {
    Assertions.assertEquals("description", repository.findOne(1L).get().getDescription());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingOneThatIsNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(repository.findOne(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_FindingOneThatIsInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(repository.findOne(1L).isPresent(), "member with id given found");
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_WhenFindOneWithNullId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.findOne(null));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingAllInRepository_Then_ReturnSetWithThatEntity() {
    Set<LabProblem> labProblems = new HashSet<>();
    labProblems.add(labProblem);
    Assertions.assertEquals(labProblems, repository.findAll());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_SavingValidEntity_Then_NumberOfElementsInTheRepositoryIncrements() {
    LabProblem labProblem = new LabProblem(12, "description2");
    labProblem.setId(2L);
    repository.save(labProblem);
    Assertions.assertEquals(2, ((Set<LabProblem>) repository.findAll()).size());
  }

  @Test
  void
      Given_LabProblemWithOneEntity_When_SavingEntityWithIdAlreadyInRepository_Then_SizeOfRepositoryRemainsOne() {
    repository.save(labProblem);
    Assertions.assertEquals(1, ((Set<LabProblem>) repository.findAll()).size());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_SavingNull_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.save(null));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingSingleEntityInRepository_Then_SizeOfRepositoryIsZero() {
    repository.delete(1L);
    Assertions.assertEquals(0, ((Set<LabProblem>) repository.findAll()).size());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnRightDataAboutTheDeletedEntity() {
    Assertions.assertEquals("description", repository.delete(1L).get().getDescription());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityNotInRepository_Then_ReturnFalse() {
    Assertions.assertFalse(repository.delete(2L).isPresent(), "member with id given not found");
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_DeletingEntityInRepository_Then_ReturnTrue() {
    Assertions.assertTrue(repository.delete(1L).isPresent(), "member with id given deleted");
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingEntityNotInRepository_Then_ReturnOptionalWithEntity() {
    LabProblem updatedLabProblem2 = new LabProblem(12, "description2");
    updatedLabProblem2.setId(2L);
    Optional<LabProblem> updateResult = repository.update(updatedLabProblem2);
    Assertions.assertTrue(updateResult.isPresent());
    Assertions.assertEquals(updatedLabProblem2, updateResult.get());
  }

  @Test
  void Given_StudentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_UpdateEntity() {
    LabProblem labProblem = new LabProblem(2, "descriptionUpdated");
    labProblem.setId(1L);
    repository.update(labProblem);
    Assertions.assertEquals("descriptionUpdated", repository.findOne(1L).get().getDescription());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingEntityInRepository_Then_ReturnOptionalWithNoValue() {
    LabProblem labProblem = new LabProblem(2, "descriptionUpdated");
    labProblem.setId(1L);
    Assertions.assertFalse(repository.update(labProblem).isPresent());
  }

  @Test
  void
      Given_StudentRepositoryWithOneEntity_When_UpdatingWithNullId_Then_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repository.update(null));
  }
}
