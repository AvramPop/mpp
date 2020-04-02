package ro.ubb.remoting.server.service.validators;

import sun.security.validator.ValidatorException;

/**
 * Interface designed to be used for validators
 *
 * @param <T> the type of the entity for which the validator is to be used
 */
public interface Validator<T> {
  /**
   * @param entity the entity to be validated
   * @throws ValidatorException in case of invalid instance of entity an exception is thrown
   */
  void validate(T entity) throws ValidatorException;
}
