package ro.ubb.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * A base class to be extended by any in ro.ubb.domain, having only an id.
 *
 * @param <ID> the type of the identifier
 */
public abstract class BaseEntity<ID> {
  private ID id;

  public ID getId() {
    return id;
  }

  public void setId(ID id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "BaseEntity{" + "id=" + id + '}';
  }

  /**
   * Create savable to file string from current instance, complying to file standards.
   *
   * @param delimiter character to separate the object members
   * @return this in file-string format
   */
  public abstract String objectToFileLine(String delimiter);

  public abstract Node objectToXMLNode(Document document);
}
