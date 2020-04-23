package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.web.dto.AssignmentDto;

@Component
public class AssignmentConverter extends BaseConverter<Assignment, AssignmentDto> {
  @Override
  public Assignment convertDtoToModel(AssignmentDto dto) {
    Assignment assignment =
        Assignment.builder()
            .grade(dto.getGrade())
            .studentId(dto.getStudentId())
            .labProblemId(dto.getLabProblemId())
            .build();
    assignment.setId(dto.getId());
    return assignment;
  }

  @Override
  public AssignmentDto convertModelToDto(Assignment assignment) {
    AssignmentDto dto =
        AssignmentDto.builder()
            .grade(assignment.getGrade())
            .studentId(assignment.getStudentId())
            .labProblemId(assignment.getLabProblemId())
            .build();
    dto.setId(assignment.getId());
    return dto;
  }
}
