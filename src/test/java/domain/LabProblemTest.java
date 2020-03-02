package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LabProblemTest {

  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String DESCRIPTION = "problemDescription";
  private static final String NEW_DESCRIPTION = "newProblemDescription";
  private static final int PROBLEM_NUMBER = 123;
  private static final int NEW_PROBLEM_NUMBER = 999;

  private LabProblem labProblem;

  @BeforeEach
  void setUp() {
    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
  }

  @AfterEach
  void tearDown() {
    labProblem = null;
  }

  @Test
  void Given_ValidLabProblem_When_GettingId_Then_GetProperId() {
    assertEquals(ID, labProblem.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidLabProblem_When_SettingId_Then_SetProperId() {
    labProblem.setId(NEW_ID);
    assertEquals(NEW_ID, labProblem.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidLabProblem_When_GettingProblemNumber_Then_GetProperProblemNumber() {
    assertEquals(PROBLEM_NUMBER, labProblem.getProblemNumber(), "Problem numbers should be equal");
  }

  @Test
  void Given_ValidLabProblem_When_SettingProblemNumber_Then_SetProperProblemNumber() {
    labProblem.setProblemNumber(NEW_PROBLEM_NUMBER);
    assertEquals(
        NEW_PROBLEM_NUMBER, labProblem.getProblemNumber(), "Problem numbers should be equal");
  }

  @Test
  void Given_ValidLabProblem_When_GettingDescription_Then_GetProperDescription() {
    assertEquals(DESCRIPTION, labProblem.getDescription(), "Descriptions should be equal");
  }

  @Test
  void Given_ValidLabProblem_When_SettingDescription_Then_SetProperDescription() {
    labProblem.setDescription(NEW_DESCRIPTION);
    assertEquals(NEW_DESCRIPTION, labProblem.getDescription(), "Descriptions should be equal");
  }
}
