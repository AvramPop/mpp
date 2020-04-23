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
  private List<AbstractMap.SimpleEntry<String, String>> sortData;

  public SortDto() {
    sortData = new ArrayList<>();
  }
}
