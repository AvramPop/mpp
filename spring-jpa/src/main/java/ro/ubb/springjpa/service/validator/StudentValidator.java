package ro.ubb.springjpa.service.validator;

import org.springframework.stereotype.Component;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.Student;

@Component
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
    if (entity.getStudentGroup() < 0) errorMessage.append("Invalid group id! ");

    char[] arrayOfSerial = entity.getSerialNumber().toCharArray();

    if (!entity.getSerialNumber().chars().allMatch(Character::isLetterOrDigit)
        || entity.getSerialNumber().length() == 0) errorMessage.append("Invalid serial number! ");

    if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());
  }
}
