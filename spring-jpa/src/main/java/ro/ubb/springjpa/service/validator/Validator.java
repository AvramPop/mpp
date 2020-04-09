package ro.ubb.springjpa.service.validator;

import ro.ubb.springjpa.exceptions.ValidatorException;

public interface Validator<T> {
  /**
   * @param entity the entity to be validated
   * @throws ValidatorException in case of invalid instance of entity an exception is thrown
   */
  void validate(T entity) throws ValidatorException;
}
