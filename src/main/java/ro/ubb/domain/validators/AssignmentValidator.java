package ro.ubb.domain.validators;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;
/**
 * AssignmentValidator has it's sole purpose to check out whether an instance of Assignment complies
 * to the requirements for such objects.
 */
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
    if (entity.getStudentId() == null) errorMessage.append("Id is null");
    else if (entity.getStudentId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getLabProblemId() == null) errorMessage.append("Id is null");
    else if (entity.getLabProblemId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getGrade() < 0 || entity.getGrade() > 10) errorMessage.append("Invalid grade! ");
    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
