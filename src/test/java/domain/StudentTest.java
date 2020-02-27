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
  public void setUp() throws Exception {
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
  }

  @AfterEach
  public void tearDown() throws Exception {
    student = null;
  }

  @Test
  public void testGetSerialNumber() throws Exception {
    assertEquals(SERIAL_NUMBER, student.getSerialNumber(), "Serial numbers should be equal");
  }

  @Test
  public void testSetSerialNumber() throws Exception {
    student.setSerialNumber(NEW_SERIAL_NUMBER);
    assertEquals(NEW_SERIAL_NUMBER, student.getSerialNumber(), "Serial numbers should be equal");
  }

  @Test
  public void testGetId() throws Exception {
    assertEquals(ID, student.getId(), "Ids should be equal");
  }

  @Test
  public void testSetId() throws Exception {
    student.setId(NEW_ID);
    assertEquals(NEW_ID, student.getId(), "Ids should be equal");
  }

  @Test
  public void testGetName() throws Exception {
    assertEquals(NAME, student.getName(), "Names should be equal");
  }

  @Test
  public void testSetName() throws Exception {
    student.setName(NEW_NAME);
    assertEquals(NEW_NAME, student.getName(), "Names should be equal");
  }

  @Test
  public void testGetGroup() throws Exception {
    assertEquals(GROUP, student.getGroup(), "Groups should be equal");
  }

  @Test
  public void testSetGroup() throws Exception {
    student.setGroup(NEW_GROUP);
    assertEquals(NEW_GROUP, student.getGroup(), "Groups should be equal");
  }
}