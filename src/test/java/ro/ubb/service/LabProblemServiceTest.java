package ro.ubb.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.LabProblemValidator;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.SortingRepository;
import ro.ubb.repository.inmemory.InMemoryRepository;

class LabProblemServiceTest {
  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String DESCRIPTION = "123123";
  private static final String NEW_DESCRIPTION = "123";
  private static final int PROBLEM_NUMBER = 123;
  private static final int NEW_PROBLEM_NUMBER = 999;
  private LabProblem labProblem;
  private LabProblemService labProblemService;
  private Validator<LabProblem> labProblemValidator;
  private SortingRepository<Long, LabProblem> labProblemRepository;

  @BeforeEach
  void setUp() {

    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
    labProblemValidator = new LabProblemValidator();
    labProblemRepository = new InMemoryRepository<>();
    labProblemService = new LabProblemService(labProblemRepository, labProblemValidator);
  }

  @AfterEach
  void tearDown() {

    labProblemService = null;
    labProblem = null;
    labProblemRepository = null;
    labProblemValidator = null;
  }

  @Test
  void Given_EmptyRepository_When_ValidLabProblemAdded_Then_AdditionWillSucceed() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setId(-1L);
    Assertions.assertThrows(
        ValidatorException.class,
        () -> labProblemService.addLabProblem(-1L, PROBLEM_NUMBER, DESCRIPTION));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidProblemNumberAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setProblemNumber(-1);
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(ID, -1, DESCRIPTION));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidDescriptionAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setDescription("");
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(ID, PROBLEM_NUMBER, ""));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_ReadingAllLabProblemEntitiesFormRepository_Then_NumberOfEntitiesReturnedIsOne() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertEquals(labProblemService.getAllLabProblems().size(), 1);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_NumberOfEntitiesReturnedIsZero() {

    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    labProblemService.deleteLabProblem(labProblem.getId());
    Assertions.assertEquals(labProblemService.getAllLabProblems().size(), 0);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNotNull() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertTrue(labProblemService.deleteLabProblem(labProblem.getId()).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalContainsTheEntity() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertEquals(
        labProblemService.deleteLabProblem(labProblem.getId()).get(), labProblem);
  }

  @Test
  void
      Given_EmptyRepository_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    Assertions.assertFalse(labProblemService.deleteLabProblem(labProblem.getId()).isPresent());
  }

  @Test
  void Given_EmptyRepository_When_DeletingANull_Then_ThenThrowsIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemService.deleteLabProblem(null));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfLabProblemEntity_Then_UpdateWillFailAndThrowsValidatorException() {
    labProblem.setId(-1L);
    Assertions.assertThrows(
        ValidatorException.class,
        () -> labProblemService.updateLabProblem(-1L, PROBLEM_NUMBER, DESCRIPTION));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidProblemNumberAttributeOfLabProblemEntity_Then_UpdateWillFailAndThrowsValidatorException() {
    labProblem.setProblemNumber(-1);
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.updateLabProblem(ID, -1, DESCRIPTION));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidDescriptionAttributeOfLabProblemEntity_Then_UpdateWillFailAndThrowsValidatorException() {
    labProblem.setDescription("");
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.updateLabProblem(ID, PROBLEM_NUMBER, ""));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityNotInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertFalse(labProblemService.deleteLabProblem(NEW_ID).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingTheEntity_Then_UpdateSucceedsReturnsEmptyOptional() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setDescription(NEW_DESCRIPTION);
    Assertions.assertFalse(
        labProblemService.updateLabProblem(ID, PROBLEM_NUMBER, NEW_DESCRIPTION).isPresent());
  }

  @Test
  void Given_LabProblemRepositoryWithOneEntity_When_UpdatingTheEntity_Then_EntityIsUpdated() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setDescription(NEW_DESCRIPTION);
    labProblemService.updateLabProblem(ID, PROBLEM_NUMBER, NEW_DESCRIPTION);
    Assertions.assertEquals(labProblem, labProblemService.getAllLabProblems().toArray()[0]);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingNonExistingEntity_Then_UpdateFailsReturnsOptionalWithEntity() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(2L);
    Assertions.assertEquals(
        labProblemService.updateLabProblem(2L, PROBLEM_NUMBER, DESCRIPTION).get(), labProblem);
  }

  @Test
  void Given_EmptyLabProblemRepository_When_FilteringByProblemNumber_Then_ReturnsEmptySet() {
    Assertions.assertEquals(labProblemService.filterByProblemNumber(PROBLEM_NUMBER).size(), 0);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FilteringByProblemNumberWhichIsInRepository_Then_SetWithOneElement() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertEquals(labProblemService.filterByProblemNumber(PROBLEM_NUMBER).size(), 1);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalContainingIt() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertEquals(
        labProblemService.getLabProblemById(labProblem.getId()).get(), labProblem);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingByIdThatEntity_Then_ReturnOptionalWithValueInside() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertTrue(labProblemService.getLabProblemById(labProblem.getId()).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingByIdOtherEntity_Then_ReturnOptionalWithNullInside() {
    labProblemService.addLabProblem(ID, PROBLEM_NUMBER, DESCRIPTION);
    Assertions.assertFalse(labProblemService.getLabProblemById(2L).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FindingByIdWithInvalidId_Then_ThrowIllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemService.getLabProblemById(-1L));
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> labProblemService.getLabProblemById(null));
  }

  @Test
  void Given_LabProblemService_When_SavingInvalidEntity_Then_ThrowsValidatorException() {
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(null, -1, ""));
  }

  @Test
  void Given_LabProblemService_When_UpdatingInvalidEntity_Then_ThrowsValidatorException() {
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.updateLabProblem(null, -1, ""));
  }
}
