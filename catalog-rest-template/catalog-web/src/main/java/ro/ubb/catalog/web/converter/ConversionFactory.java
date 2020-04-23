package ro.ubb.catalog.web.converter;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.web.dto.DoubleDto;
import ro.ubb.catalog.web.dto.PairDto;

import java.util.AbstractMap;

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
}
