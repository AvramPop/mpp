package service.validators;

import domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentValidatorTest {

    private static final Long ID = 1L;
    private static final Long NEW_ID = 2L;
    private static final String SERIAL_NUMBER = "sn01";
    private static final String NEW_SERIAL_NUMBER = "sn02";
    private static final String NAME = "studentName";
    private static final int GROUP = 123;


    Student student;
    StudentValidator validator;

    @Before
    public void setUp() throws Exception {
        student = new Student(SERIAL_NUMBER, NAME, GROUP);
        student.setId(ID);
        validator = new StudentValidator();
    }

    @After
    public void tearDown() throws Exception {
        student = null;
    }

    @Test
    public void validateCorrectStudent() throws Exception {

    }
}