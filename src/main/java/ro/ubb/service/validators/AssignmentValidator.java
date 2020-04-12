package ro.ubb.service.validators;

import org.springframework.stereotype.Component;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;
/**
 * AssignmentValidator has it's sole purpose to check out whether an instance of Assignment complies
 * to the requirements for such objects.
 */
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
    if (entity.getStudent().getId() == null) errorMessage.append("Id is null");
    else if (entity.getStudent().getId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getLabProblem().getId() == null) errorMessage.append("Id is null");
    else if (entity.getLabProblem().getId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getGrade() < 0 || entity.getGrade() > 10) errorMessage.append("Invalid grade! ");
    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
