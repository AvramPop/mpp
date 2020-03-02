package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {

  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String SERIAL_NUMBER = "sn01";
  private static final String NEW_SERIAL_NUMBER = "sn02";
  private static final String NAME = "studentName";
  private static final String NEW_NAME = "newStudentName";
  private static final int GROUP = 123;
  private static final int NEW_GROUP = 999;

  private Student student;

  @BeforeEach
  void setUp() {
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
  }

  @AfterEach
  void tearDown() {
    student = null;
  }

  @Test
  void Given_ValidStudent_When_GettingSerialNumber_Then_GetProperSerialNumber() {
    assertEquals(SERIAL_NUMBER, student.getSerialNumber(), "Serial numbers should be equal");
  }

  @Test
  void Given_ValidStudent_When_SettingSerialNumber_Then_SetProperSerialNumber() {
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    assertEquals(NEW_SERIAL_NUMBER, student.getSerialNumber(), "Serial numbers should be equal");
  }

  @Test
  void Given_ValidStudent_When_GettingId_Then_GetProperId() {
    assertEquals(ID, student.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidStudent_When_SettingId_Then_SetProperId() {
    student.setId(NEW_ID);
    assertEquals(NEW_ID, student.getId(), "Ids should be equal");
  }

  @Test
  void Given_ValidStudent_When_GettingName_Then_GetProperName() {
    assertEquals(NAME, student.getName(), "Names should be equal");
  }

  @Test
  void Given_ValidStudent_When_SettingName_Then_SetProperName() {
    student.setName(NEW_NAME);
    assertEquals(NEW_NAME, student.getName(), "Names should be equal");
  }

  @Test
  void Given_ValidStudent_When_GettingGroup_Then_GetProperGroup() {
    assertEquals(GROUP, student.getGroup(), "Groups should be equal");
  }

  @Test
  void Given_ValidStudent_When_SettingGroup_Then_SetProperGroup() {
    student.setGroup(NEW_GROUP);
    assertEquals(NEW_GROUP, student.getGroup(), "Groups should be equal");
  }
}
