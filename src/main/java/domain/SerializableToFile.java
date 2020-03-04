package domain;

import domain.exceptions.RepositoryException;

/**
 * Interface to be implemented by any in domain, standardizing object writing and reading from plain text file
 * @param <T> the class implementing
 */

public interface SerializableToFile<T> {
  /**
   * Parse given string to object of the type of the generic
   *
   * @param fileLine given string to parse
   * @param delimiter character between object fields in fileLine
   * @return object of type given with member as parsed from string
   */
  T objectFromFileLine(String fileLine, String delimiter) throws RepositoryException;

  /**
   * Create savable to file string from current instance, complying to file standards.
   *
   * @param delimiter character to separate the object members
   * @return this in file-string format
   */
  String objectToFileLine(String delimiter);

}
