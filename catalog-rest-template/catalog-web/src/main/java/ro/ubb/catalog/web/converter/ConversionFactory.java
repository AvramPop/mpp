package ro.ubb.catalog.web.converter;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.web.dto.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversionFactory {
  public DoubleDto convertDoubleToDto(Double averageGrade) {
    return DoubleDto.builder().value(averageGrade).build();
  }

  public PairDto<Long, Long> convertIdToDto(
      AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned) {
    return PairDto.<Long, Long>builder()
        .key(idOfLabProblemMostAssigned.getKey())
        .value(idOfLabProblemMostAssigned.getValue())
        .build();
  }

  public PairDto<Long, Double> convertMeanToDto(
      AbstractMap.SimpleEntry<Long, Double> greatestMean) {
    return PairDto.<Long, Double>builder()
        .key(greatestMean.getKey())
        .value(greatestMean.getValue())
        .build();
  }

  public PagedAssignmentsDto convertPagedAssignmentsToDtos(Page<Assignment> models) {
    return new PagedAssignmentsDto(
        models.getContent().stream().map(this::convertAssignmentToDto).collect(Collectors.toList()),
        models.hasNext(),
        models.hasPrevious(),
        models.getNumber());
  }

  public PagedStudentsDto convertPagedStudentsToDtos(Page<Student> models) {
    return new PagedStudentsDto(
        models.getContent().stream().map(this::convertStudentToDto).collect(Collectors.toList()),
        models.hasNext(),
        models.hasPrevious(),
        models.getNumber());
  }

  public PagedLabProblemsDto convertPagedLabProblemsToDtos(Page<LabProblem> models) {
    return new PagedLabProblemsDto(
        models.getContent().stream().map(this::convertLabProblemToDto).collect(Collectors.toList()),
        models.hasNext(),
        models.hasPrevious(),
        models.getNumber());
  }

  private AssignmentDto convertAssignmentToDto(Assignment assignment) {
    AssignmentDto dto =
        AssignmentDto.builder()
            .grade(assignment.getGrade())
            .studentId(assignment.getStudentId())
            .labProblemId(assignment.getLabProblemId())
            .build();
    dto.setId(assignment.getId());
    return dto;
  }

  private StudentDto convertStudentToDto(Student student) {
    StudentDto dto =
        StudentDto.builder()
            .serialNumber(student.getSerialNumber())
            .name(student.getName())
            .studentGroup(student.getGroupNumber())
            .build();
    dto.setId(student.getId());
    return dto;
  }

  private LabProblemDto convertLabProblemToDto(LabProblem labProblem) {
    LabProblemDto dto =
        LabProblemDto.builder()
            .problemNumber(labProblem.getProblemNumber())
            .description(labProblem.getDescription())
            .build();
    dto.setId(labProblem.getId());
    return dto;
  }
}
