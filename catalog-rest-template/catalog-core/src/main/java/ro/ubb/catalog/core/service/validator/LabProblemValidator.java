package ro.ubb.catalog.core.service.validator;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;

@Component
public class LabProblemValidator implements Validator<LabProblem> {

  /**
   * Checks whether the given LabProblem instance is valid.
   *
   * @param entity the LabProblem to be validated
   * @throws ValidatorException if at least one of the criteria for creating the LabProblem is
   *     violated
   */
  @Override
  public void validate(LabProblem entity) throws ValidatorException {
    StringBuilder errorMessage = new StringBuilder();
    if (entity.getId() == null) errorMessage.append("Id is null");
    else if (entity.getId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getProblemNumber() < 0) errorMessage.append("Invalid problem number! ");
    if (entity.getDescription().isEmpty()) errorMessage.append("Invalid description! ");

    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
