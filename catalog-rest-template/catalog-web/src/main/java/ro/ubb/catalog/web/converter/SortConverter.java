package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.web.dto.SortDto;

@Component
public class SortConverter {
  public Sort convertDtoToSort(SortDto sortDto) {
    Sort sort =
        new Sort(stringToDirection(sortDto.getKeys().get(0)), sortDto.getValues().get(0));
    for (int i = 1; i < sortDto.getKeys().size(); i++) {
      sort.and(
          new Sort(stringToDirection(sortDto.getKeys().get(i)), sortDto.getValues().get(i)));
    }
    sort.setClassName(sortDto.getClassName());
    return sort;
  }

  private Sort.Direction stringToDirection(String direction){
    if(direction.equals("ASC")) return Sort.Direction.ASC;
    else return Sort.Direction.DESC;
  }
}
