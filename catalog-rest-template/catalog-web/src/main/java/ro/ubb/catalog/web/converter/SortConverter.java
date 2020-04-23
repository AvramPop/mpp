package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.web.dto.SortDto;

@Component
public class SortConverter {
  public Sort convertDtoToSort(SortDto sortDto) {
    Sort sort =
        new Sort(sortDto.getSortData().get(0).getKey(), sortDto.getSortData().get(0).getValue());
    for (int i = 1; i < sortDto.getSortData().size(); i++) {
      sort.and(
          new Sort(sortDto.getSortData().get(i).getKey(), sortDto.getSortData().get(i).getValue()));
    }
    sort.setClassName(sortDto.getClassName());
    return sort;
  }
}
