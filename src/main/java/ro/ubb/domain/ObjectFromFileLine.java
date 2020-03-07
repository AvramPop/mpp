package ro.ubb.domain;

/**
 * Functional interface converts string with a given delimiter to object of type T
 *
 * @param <T> the class implementing
 */
public interface ObjectFromFileLine<T> {
  /**
   * Convert the input string using a delimiter for the parameters
   *
   * @param line the string which stores the information about the parameters
   * @param delimiter the delimiter for that class in the line
   * @return the new Entity of type {@code T}
   */
  T convert(String line, String delimiter);
}
