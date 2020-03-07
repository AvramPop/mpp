package domain;
/**
 * A base class to be extended by any in domain, having only an id.
 *
 * @param <ID> the type of the identifier
 */
public abstract class BaseEntity<ID>{
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

//  /**
//   * Parse given string to object of the type of the generic
//   *
//   * @param fileLine given string to parse
//   * @param delimiter character between object fields in fileLine
//   * @return object of type given with member as parsed from string
//   */
//  @Override
//  public abstract BaseEntity<ID> objectFromFileLine(String fileLine, String delimiter);

  /**
   * Create savable to file string from current instance, complying to file standards.
   *
   * @param delimiter character to separate the object members
   * @return this in file-string format
   */
  public abstract String objectToFileLine(String delimiter);

  //  /**
//   * Parse given string to object of the type of the generic
//   *
//   * @param fileLine given string to parse
//   * @param delimiter character between object fields in fileLine
//   * @return object of type given with member as parsed from string
//   */
//  T objectFromFileLine(String fileLine, String delimiter) throws RepositoryException;


  public static <T> ObjectFromFileLine<T> objectFromFileLine(){
    throw new IllegalStateException(
        "Method has not been overwritten in child");
  }
}
