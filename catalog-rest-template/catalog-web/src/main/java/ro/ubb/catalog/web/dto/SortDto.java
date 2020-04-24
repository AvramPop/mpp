package ro.ubb.catalog.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Data
public class SortDto {
  private String className;
  private List<String> keys;
  private List<String> values;
  public SortDto() {
    keys = new ArrayList<>();
    values = new ArrayList<>();
  }
}
