package ro.ubb.domain.validators;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.exceptions.ValidatorException;

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

        if (errorMessage.length() > 0) throw new ValidatorException(errorMessage.toString());

    }
}
