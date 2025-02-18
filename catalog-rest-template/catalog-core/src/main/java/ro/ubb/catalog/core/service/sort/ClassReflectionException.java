package ro.ubb.catalog.core.service.sort;

public class ClassReflectionException extends RuntimeException {
  public ClassReflectionException(String message) {
    super(message);
  }

  public ClassReflectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClassReflectionException(Throwable cause) {
    super(cause);
  }
}
