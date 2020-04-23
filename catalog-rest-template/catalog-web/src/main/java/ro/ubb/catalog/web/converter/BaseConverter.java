package ro.ubb.catalog.web.converter;

import ro.ubb.catalog.core.model.BaseEntity;
import ro.ubb.catalog.web.dto.BaseDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** Created by radu. */
public abstract class BaseConverter<Model extends BaseEntity<Long>, Dto extends BaseDto>
    implements Converter<Model, Dto> {

  public List<Long> convertModelsToIDs(List<Model> models) {
    return models.stream().map(model -> model.getId()).collect(Collectors.toList());
  }

  public List<Long> convertDTOsToIDs(List<Dto> dtos) {
    return dtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());
  }

  public List<Dto> convertModelsToDtos(Collection<Model> models) {
    return models.stream().map(this::convertModelToDto).collect(Collectors.toList());
  }
}
