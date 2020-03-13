package ro.ubb.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Objects;
/** A student having group (positive integer), name (nonempty) and serialNumber (nonempty). */
public class Student extends BaseEntity<Long> {
  private String serialNumber;
  private String name;
  private int group;

  public Student() {
    serialNumber = "";
    name = "";
    group = -1;
    setId(-1L);
  }

  public Student(String serialNumber, String name, int group) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.group = group;
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

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
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
        + group
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
    return Objects.hash(serialNumber, name, group);
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
        + this.group;
  }

  @Override
  public Node objectToXMLNode(Document document) {
    Element studentElement = document.createElement("student");
    studentElement.setAttribute("Id", this.getId().toString());
    appendChildWithTextToNode(document, studentElement, "serialNumber", this.serialNumber);
    appendChildWithTextToNode(document, studentElement, "name", this.name);
    appendChildWithTextToNode(document, studentElement, "group", Integer.toString(this.group));
    return studentElement;
  }

  private void appendChildWithTextToNode(
      Document document, Node parentNode, String tagName, String textContent) {

    Element element = document.createElement(tagName);
    element.setTextContent(textContent);
    parentNode.appendChild(element);
  }
}
