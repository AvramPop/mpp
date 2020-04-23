package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.web.dto.LabProblemDto;

@Component
public class LabProblemConverter extends BaseConverter<LabProblem, LabProblemDto> {
  @Override
  public LabProblem convertDtoToModel(LabProblemDto dto) {
    LabProblem labProblem =
        LabProblem.builder()
            .problemNumber(dto.getProblemNumber())
            .description(dto.getDescription())
            .build();
    labProblem.setId(dto.getId());
    return labProblem;
  }

  @Override
  public LabProblemDto convertModelToDto(LabProblem labProblem) {
    LabProblemDto dto =
        LabProblemDto.builder()
            .problemNumber(labProblem.getProblemNumber())
            .description(labProblem.getDescription())
            .build();
    dto.setId(labProblem.getId());
    return dto;
  }
}
