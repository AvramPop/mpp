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
}
