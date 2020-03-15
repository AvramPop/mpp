package ro.ubb.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssignmentTest {
  private static final Long NEW_ID = 2L;
  private static final Long ID = 1L;
  private static final Long STUDENT_ID = 1L;
  private static final Long LAB_PROBLEM_ID = 1L;
  private static final int GRADE = 7;
  private static final int NEW_GRADE = 7;
  private static final Long NEW_STUDENT_ID = 2L;
  private static final Long NEW_LAB_PROBLEM_ID = 2L;

  private Assignment assignment;

  @BeforeEach
  void setUp() {
    assignment = new Assignment(STUDENT_ID, LAB_PROBLEM_ID, GRADE);
    assignment.setId(ID);
  }

  @AfterEach
  void tearDown() {
    assignment = null;
  }

  @Test
  void Given_ValidAssignment_When_GettingId_Then_GetProperId() {
    assertEquals(ID, assignment.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_SettingId_Then_SetProperId() {
    assignment.setId(NEW_ID);
    assertEquals(NEW_ID, assignment.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_GettingStudentId_Then_GetProperStudentId() {
    assertEquals(STUDENT_ID, assignment.getStudentId(), "student ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_SettingStudentId_Then_SetProperStudentId() {
    assignment.setStudentId(NEW_STUDENT_ID);
    assertEquals(NEW_STUDENT_ID, assignment.getStudentId(), "student ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_GettingLabProblemId_Then_GetProperLabProblemId() {
    assertEquals(LAB_PROBLEM_ID, assignment.getLabProblemId(), "LabProblem Ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_SettingLabProblemIdThen_SetProperLabProblemId() {
    assignment.setLabProblemId(NEW_LAB_PROBLEM_ID);
    assertEquals(
        NEW_LAB_PROBLEM_ID, assignment.getLabProblemId(), "LabProblem Ids should be equal");
  }

  @Test
  void Given_ValidAssignment_When_GettingGrade_Then_GetProperGrade() {
    assertEquals(GRADE, assignment.getGrade(), "Grades should be equal");
  }

  @Test
  void Given_ValidAssignment_When_SettingGrade_Then_SetProperGrade() {
    assignment.setGrade(NEW_GRADE);
    assertEquals(NEW_GRADE, assignment.getGrade(), "grades should be equal");
  }

  @Test
  void objectToFileLine() {
    assertEquals(assignment.objectToFileLine(";"), "1;1;1;7");
  }
}
