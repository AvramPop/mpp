package ro.ubb.domain;

import org.w3c.dom.Element;

/**
 * Functional interface converts XML element to object of type T
 *
 * @param <T> the class implementing
 */
public interface ObjectFromXMLFile<T> {
  /**
   * Convert the input string using a delimiter for the parameters
   *
   * @param element xml entity to convert from
   * @return the new Entity of type {@code T}
   */
  T convert(Element element);
}
