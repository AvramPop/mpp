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
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.service.AssignmentService;
import ro.ubb.catalog.core.service.LabProblemService;
import ro.ubb.catalog.web.converter.LabProblemConverter;
import ro.ubb.catalog.web.dto.LabProblemDto;

import java.util.*;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LabProblemControllerTest {

  private MockMvc mockMvc;

  @InjectMocks private LabProblemController labProblemController;

  @Mock private LabProblemService labProblemService;

  @Mock private LabProblemConverter labProblemConverter;

  private LabProblem labProblem1;
  private LabProblem labProblem2;
  private LabProblemDto labProblemDto1;
  private LabProblemDto labProblemDto2;

  @Before
  public void setup() throws Exception {
    initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(labProblemController).build();
    initData();
  }

  @Test
  public void getLabProblems() throws Exception {
    List<LabProblem> labProblems = Arrays.asList(labProblem1, labProblem2);

    List<LabProblemDto> labProblemDtos =
        new ArrayList<>(Arrays.asList(labProblemDto1, labProblemDto2));
    when(labProblemService.getAllLabProblems()).thenReturn(labProblems);
    when(labProblemConverter.convertModelsToDtos(labProblems)).thenReturn(labProblemDtos);

    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders.get("/labs"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.labProblems", hasSize(2)))
        .andExpect(jsonPath("$.labProblems[0].description", anyOf(is("sn1"), is("sn2"))));

    String result = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println(result);

    verify(labProblemService, times(1)).getAllLabProblems();
    verify(labProblemConverter, times(1)).convertModelsToDtos(labProblems);
    verifyNoMoreInteractions(labProblemService, labProblemConverter);


  }
  @Test
  public void updateLabProblem() throws Exception {

    when(labProblemService.updateLabProblem(labProblem1.getId(), labProblem1))
        .thenReturn(true);

    when(labProblemConverter.convertDtoToModel(any(LabProblemDto.class))).thenReturn(labProblem1);


    ResultActions resultActions = mockMvc
        .perform(MockMvcRequestBuilders
            .put("/labs/{labProblemId}", labProblem1.getId(), labProblemDto1)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(toJsonString(labProblemDto1)))

        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.statusCode", is(200)));

    verify(labProblemService, times(1)).updateLabProblem(labProblem1.getId(), labProblem1);
    verify(labProblemConverter, times(1)).convertDtoToModel(any(LabProblemDto.class));
    verifyNoMoreInteractions(labProblemService, labProblemConverter);
  }

  private String toJsonString(Map<String, LabProblemDto> labProblemDtoMap) {
    try {
      return new ObjectMapper().writeValueAsString(labProblemDtoMap);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private String toJsonString(LabProblemDto labProblemDto) {
    try {
      System.out.println(new ObjectMapper().writeValueAsString(labProblemDto));
      return new ObjectMapper().writeValueAsString(labProblemDto);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void createLabProblem() throws Exception {
    when(labProblemConverter.convertDtoToModel(any(LabProblemDto.class))).thenReturn(labProblem1);
    ResultActions resultActions =
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .post("/labs", labProblem1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(toJsonString(labProblemDto1)))
            .andExpect(status().isOk());
    verify(labProblemService, times(1)).saveLabProblem(labProblem1);
    verify(labProblemConverter, times(1)).convertDtoToModel(any(LabProblemDto.class));
    verifyNoMoreInteractions(labProblemService, labProblemConverter);

  }

  @Test
  public void deleteLabProblem() throws Exception {
    when(labProblemService.deleteLabProblem(labProblem1.getId())).thenReturn(true);

    ResultActions resultActions =
        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/labs/{labProblemId}", labProblem1.getId())
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.statusCode", is(200)));

    verify(labProblemService, times(1)).deleteLabProblem(labProblem1.getId());
  }

  private void initData() {
    labProblem1 = LabProblem.builder().description("sn1").problemNumber(1).build();
    labProblem1.setId(1l);
    labProblem2 = LabProblem.builder().description("sn2").problemNumber(2).build();
    labProblem2.setId(2l);

    labProblemDto1 = createLabProblemDto(labProblem1);
    labProblemDto2 = createLabProblemDto(labProblem2);
  }

  private LabProblemDto createLabProblemDto(LabProblem labProblem) {
    LabProblemDto labProblemDto =
        LabProblemDto.builder()
            .description(labProblem.getDescription())
            .problemNumber(labProblem.getProblemNumber())
            .build();
    labProblemDto.setId(labProblem.getId());
    return labProblemDto;
  }
}
