package service.validators;

import domain.Student;
import domain.exceptions.ValidatorException;

import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class StudentValidator implements Validator<Student> {
    @Override
    public void validate(Student entity) throws ValidatorException {
        StringBuilder _error = new StringBuilder("");
        if (entity.getId() < 0)
            _error.append("Invalid id! ");
        if (entity.getName().equals(""))
            _error.append("Invalid name! ");
        if (entity.getGroup() < 0)
            _error.append("Invalid group id! ");

        char[] arrayOfSerial = entity.getSerialNumber().toCharArray();

        if(entity.getSerialNumber().chars().allMatch(Character::isDigit))
            _error.append("Invalid serial number! ");

        if (_error.length() > 0)
            throw new ValidatorException(_error.toString());
        return;
    }
}
