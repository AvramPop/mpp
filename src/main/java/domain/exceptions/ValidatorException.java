package domain.exceptions;

/** RepositoryException wraps RuntimeException and is to signal out validation exceptions. */
public class ValidatorException extends RuntimeException {
  public ValidatorException(String message) {
    super(message);
  }

  public ValidatorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidatorException(Throwable cause) {
    super(cause);
  }
}
