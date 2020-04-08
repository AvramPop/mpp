package ro.ubb.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.persistence.Entity;
import java.util.Objects;
/** A student having group (positive integer), name (nonempty) and serialNumber (nonempty). */
@Entity
public class Student extends BaseEntity<Long> {
  private String serialNumber;
  private String name;
  private Integer groupNumber;

  public Student() {
    serialNumber = "";
    name = "";
    groupNumber = -1;
    setId(-1L);
  }

  public Student(String serialNumber, String name, int groupNumber) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.groupNumber = groupNumber;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getGroupNumber() {
    return groupNumber;
  }

  public void setGroupNumber(int groupNumber) {
    this.groupNumber = groupNumber;
  }

  @Override
  public String toString() {
    return "Student{"
        + "id="
        + getId()
        + ", serialNumber='"
        + serialNumber
        + '\''
        + ", name='"
        + name
        + '\''
        + ", group="
        + groupNumber
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return getId().equals(student.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(serialNumber, name, groupNumber);
  }

  //  /**
  //   * Parse given string to object of the type of the generic
  //   *
  //   * @param fileLine given string to parse
  //   * @param delimiter character between object fields in fileLine
  //   * @return object of type given with member as parsed from string
  //   */

  @Override
  public String objectToFileLine(String delimiter) {
    return this.getId()
        + delimiter
        + this.serialNumber
        + delimiter
        + this.name
        + delimiter
        + this.groupNumber;
  }

  @Override
  public Node objectToXMLNode(Document document) {
    Element studentElement = document.createElement("student");
    studentElement.setAttribute("Id", this.getId().toString());
    appendChildWithTextToNode(document, studentElement, "serialNumber", this.serialNumber);
    appendChildWithTextToNode(document, studentElement, "name", this.name);
    appendChildWithTextToNode(
        document, studentElement, "group", Integer.toString(this.groupNumber));
    return studentElement;
  }

  private void appendChildWithTextToNode(
      Document document, Node parentNode, String tagName, String textContent) {

    Element element = document.createElement(tagName);
    element.setTextContent(textContent);
    parentNode.appendChild(element);
  }
}
