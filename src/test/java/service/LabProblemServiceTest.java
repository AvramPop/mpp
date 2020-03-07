package service;

import domain.LabProblem;
import domain.exceptions.ValidatorException;
import domain.validators.LabProblemValidator;
import domain.validators.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;
import repository.Repository;

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
  private Repository<Long, LabProblem> labProblemRepository;

  @BeforeEach
  void setUp() {

    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
    labProblemValidator = new LabProblemValidator();
    labProblemRepository = new InMemoryRepository<>(labProblemValidator);
    labProblemService = new LabProblemService(labProblemRepository);
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
    labProblemService.addLabProblem(labProblem);
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidIdAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setId(-1L);
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(labProblem));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidProblemNumberAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setProblemNumber(-1);
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(labProblem));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidDescriptionAttributeOfLabProblemEntity_Then_AdditionWillFailAndThrowsValidatorException() {
    labProblem.setDescription("");
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.addLabProblem(labProblem));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_ReadingAllLabProblemEntitiesFormRepository_Then_NumberOfEntitiesReturnedIsOne() {
    labProblemService.addLabProblem(labProblem);
    Assertions.assertEquals(labProblemService.getAllLabProblems().size(), 1);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_NumberOfEntitiesReturnedIsZero() {

    labProblemService.addLabProblem(labProblem);
    labProblemService.deleteLabProblem(labProblem.getId());
    Assertions.assertEquals(labProblemService.getAllLabProblems().size(), 0);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalIsNotNull() {
    labProblemService.addLabProblem(labProblem);
    Assertions.assertTrue(labProblemService.deleteLabProblem(labProblem.getId()).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityInsideTheRepository_Then_TheReturnedOptionalContainsTheEntity() {
    labProblemService.addLabProblem(labProblem);
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
        ValidatorException.class, () -> labProblemService.updateLabProblem(labProblem));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidProblemNumberAttributeOfLabProblemEntity_Then_UpdateWillFailAndThrowsValidatorException() {
    labProblem.setProblemNumber(-1);
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.updateLabProblem(labProblem));
  }

  @Test
  void
      Given_EmptyRepository_When_InvalidDescriptionAttributeOfLabProblemEntity_Then_UpdateWillFailAndThrowsValidatorException() {
    labProblem.setDescription("");
    Assertions.assertThrows(
        ValidatorException.class, () -> labProblemService.updateLabProblem(labProblem));
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_DeletingAnEntityNotInsideTheRepository_Then_TheReturnedOptionalIsNull() {
    labProblemService.addLabProblem(labProblem);
    Assertions.assertFalse(labProblemService.deleteLabProblem(NEW_ID).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingTheEntity_Then_UpdateSucceedsReturnsEmptyOptional() {
    labProblemService.addLabProblem(labProblem);
    labProblem.setDescription(NEW_DESCRIPTION);
    Assertions.assertFalse(labProblemService.updateLabProblem(labProblem).isPresent());
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_UpdatingNonExistingEntity_Then_UpdateFailsReturnsOptionalWithEntity() {
    labProblemService.addLabProblem(labProblem);
    labProblem.setId(2L);
    Assertions.assertEquals(labProblemService.updateLabProblem(labProblem).get(), labProblem);
  }

  @Test
  void Given_EmptyLabProblemRepository_When_FilteringByProblemNumber_Then_ReturnsEmptySet() {
    Assertions.assertEquals(labProblemService.filterByProblemNumber(PROBLEM_NUMBER).size(), 0);
  }

  @Test
  void
      Given_LabProblemRepositoryWithOneEntity_When_FilteringByProblemNumberWhichIsInRepository_Then_SetWithOneElement() {
    labProblemService.addLabProblem(labProblem);
    Assertions.assertEquals(labProblemService.filterByProblemNumber(PROBLEM_NUMBER).size(), 1);
  }
}
