package ro.ubb.socket.common.domain;

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

  public abstract String objectToFileLine();
}
