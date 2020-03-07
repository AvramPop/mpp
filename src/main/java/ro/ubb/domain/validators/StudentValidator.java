package ro.ubb.domain.validators;

import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ValidatorException;

/**
 * StudentValidator has it's sole purpose to check out whether an instance of Student complies to
 * the requirements for such objects.
 */
public class StudentValidator implements Validator<Student> {

  /**
   * Checks whether the given Student instance is valid.
   *
   * @param entity the Student to be validated
   * @throws ValidatorException if at least one of the criteria for creating the Student is violated
   */
  @Override
  public void validate(Student entity) throws ValidatorException {
    StringBuilder errorMessage = new StringBuilder();
    if (entity.getId() == null) errorMessage.append("Id is null");
    else if (entity.getId() < 0) errorMessage.append("Invalid id! ");
    if (entity.getName().equals("")) errorMessage.append("Invalid name! ");
    if (entity.getGroup() < 0) errorMessage.append("Invalid group id! ");

    char[] arrayOfSerial = entity.getSerialNumber().toCharArray();

    if (!entity.getSerialNumber().chars().allMatch(Character::isLetterOrDigit)
        || entity.getSerialNumber().length() == 0) errorMessage.append("Invalid serial number! ");

    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
