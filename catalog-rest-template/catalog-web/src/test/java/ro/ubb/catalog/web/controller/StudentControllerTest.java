package ro.ubb.catalog.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.AssignmentService;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.web.converter.StudentConverter;
import ro.ubb.catalog.web.dto.StudentDto;

import java.util.*;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudentControllerTest {

  private MockMvc mockMvc;

  @InjectMocks private StudentController studentController;

  @Mock private StudentService studentService;

  @Mock private StudentConverter studentConverter;

  private Student student1;
  private Student student2;
  private StudentDto studentDto1;
  private StudentDto studentDto2;

  @Before
  public void setup() throws Exception {
    initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    initData();
  }

  @Test
  public void getStudents() throws Exception {
    List<Student> students = Arrays.asList(student1, student2);

    List<StudentDto> studentDtos =
        new ArrayList<>(Arrays.asList(studentDto1, studentDto2));
    when(studentService.getAllStudents()).thenReturn(students);
    when(studentConverter.convertModelsToDtos(students)).thenReturn(studentDtos);

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.students", hasSize(2)))
        .andExpect(jsonPath("$.students[0].name", anyOf(is("s1"), is("s2"))));

    String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println(result);

    verify(studentService, times(1)).getAllStudents();
    verify(studentConverter, times(1)).convertModelsToDtos(students);
    verifyNoMoreInteractions(studentService, studentConverter);


  }
  @Test
  public void updateStudent() throws Exception {

    when(studentService.updateStudent(student1.getId(), student1))
        .thenReturn(true);

    when(studentConverter.convertDtoToModel(any(StudentDto.class))).thenReturn(student1);


    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders
            .put("/students/{studentId}", student1.getId(), studentDto1)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(toJsonString(studentDto1)))

        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.statusCode", is(200)));

    verify(studentService, times(1)).updateStudent(student1.getId(), student1);
    verify(studentConverter, times(1)).convertDtoToModel(any(StudentDto.class));
    verifyNoMoreInteractions(studentService, studentConverter);
  }

  private String toJsonString(Map<String, StudentDto> studentDtoMap) {
    try {
      return new ObjectMapper().writeValueAsString(studentDtoMap);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private String toJsonString(StudentDto studentDto) {
    try {
      System.out.println(new ObjectMapper().writeValueAsString(studentDto));
      return new ObjectMapper().writeValueAsString(studentDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void createStudent() throws Exception {
    when(studentConverter.convertDtoToModel(any(StudentDto.class))).thenReturn(student1);
    ResultActions resultActions =
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/students", student1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(toJsonString(studentDto1)))
            .andExpect(status().isOk());
    verify(studentService, times(1)).saveStudent(student1);
    verify(studentConverter, times(1)).convertDtoToModel(any(StudentDto.class));
    verifyNoMoreInteractions(studentService, studentConverter);

  }

  @Test
  public void deleteStudent() throws Exception {
    when(studentService.deleteStudent(student1.getId())).thenReturn(true);

    ResultActions resultActions =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/students/{studentId}", student1.getId())
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.statusCode", is(200)));

    verify(studentService, times(1)).deleteStudent(student1.getId());
  }

  private void initData() {
    student1 = Student.builder().serialNumber("sn1").name("s1").groupNumber(1).build();
    student1.setId(1l);
    student2 = Student.builder().serialNumber("sn2").name("s2").groupNumber(2).build();
    student2.setId(2l);

    studentDto1 = createStudentDto(student1);
    studentDto2 = createStudentDto(student2);
  }

  private StudentDto createStudentDto(Student student) {
    StudentDto studentDto =
        StudentDto.builder()
            .serialNumber(student.getSerialNumber())
            .name(student.getName())
            .studentGroup(student.getGroupNumber())
            .build();
    studentDto.setId(student.getId());
    return studentDto;
  }
}
