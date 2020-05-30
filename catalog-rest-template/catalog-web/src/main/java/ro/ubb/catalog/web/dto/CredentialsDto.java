package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString()
@Builder
public class CredentialsDto {
  private String type;
}
