package ro.ubb.catalog.core.service.validator;

public interface Validator<T> {
  /**
   * @param entity the entity to be validated
   * @throws ValidatorException in case of invalid instance of entity an exception is thrown
   */
  void validate(T entity) throws ValidatorException;
}
