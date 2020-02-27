package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabProblemTest {

  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String DESCRIPTION = "problemDescription";
  private static final String NEW_DESCRIPTION = "newProblemDescription";
  private static final int PROBLEM_NUMBER = 123;
  private static final int NEW_PROBLEM_NUMBER = 999;

  private LabProblem labProblem;

  @BeforeEach
  public void setUp() throws Exception {
    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
  }

  @AfterEach
  public void tearDown() throws Exception {
    labProblem = null;
  }

  @Test
  public void testGetId() throws Exception {
    assertEquals(ID, labProblem.getId(), "Ids should be equal");
  }

  @Test
  public void testSetId() throws Exception {
    labProblem.setId(NEW_ID);
    assertEquals(NEW_ID, labProblem.getId(), "Ids should be equal");
  }

  @Test
  void getProblemNumber() {
    assertEquals(PROBLEM_NUMBER, labProblem.getProblemNumber(), "Problem numbers should be equal");
  }

  @Test
  void setProblemNumber() {
    labProblem.setProblemNumber(NEW_PROBLEM_NUMBER);
    assertEquals(NEW_PROBLEM_NUMBER, labProblem.getProblemNumber(), "Problem numbers should be equal");

  }

  @Test
  void getDescription() {
    assertEquals(DESCRIPTION, labProblem.getDescription(), "Descriptions should be equal");
  }

  @Test
  void setDescription() {
    labProblem.setDescription(NEW_DESCRIPTION);
    assertEquals(NEW_DESCRIPTION, labProblem.getDescription(), "Descriptions should be equal");
  }
}