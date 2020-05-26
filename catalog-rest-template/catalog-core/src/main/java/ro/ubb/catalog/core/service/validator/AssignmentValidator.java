package ro.ubb.catalog.core.service.validator;

import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Assignment;

@Component
public class AssignmentValidator implements Validator<Assignment> {
  /**
   * @param entity the entity to be validated
   * @throws ValidatorException in case of invalid instance of entity an exception is thrown
   */
  @Override
  public void validate(Assignment entity) throws ValidatorException {
    StringBuilder errorMessage = new StringBuilder();
    if (entity.getId() == null) errorMessage.append("Id is null");
    else if (entity.getId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getStudent() == null) errorMessage.append("Id is null");
    if (entity.getLabProblem() == null) errorMessage.append("Id is null");
    if (entity.getGrade() < 0 || entity.getGrade() > 10) errorMessage.append("Invalid grade! ");
    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
