package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PagedAssignmentsDto {
  private List<AssignmentDto> assignments;
  private boolean hasNextPage;
  private boolean hasPreviousPage;
  private int pageNumber;
}
