package ro.ubb.domain;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * A base class to be extended by any in ro.ubb.domain, having only an id.
 *
 * @param <ID> the type of the identifier
 */
@MappedSuperclass
public class BaseEntity<ID> {
  @Id private ID id;

  public ID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseEntity<?> that = (BaseEntity<?>) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public void setId(ID id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "BaseEntity{" + "id=" + id + '}';
  }
}
