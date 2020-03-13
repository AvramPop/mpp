package ro.ubb.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Objects;

public class Assignment extends BaseEntity<Long> {

  private Long studentId;
  private Long labProblemId;
  private int grade;

  public Assignment() {}

  public Assignment(Long studentId, Long labProblemId) {
    this.studentId = studentId;
    this.labProblemId = labProblemId;
  }

  public Assignment(Long studentId, Long labProblemId, int grade) {
    this.studentId = studentId;
    this.labProblemId = labProblemId;
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "Assignment{ "
        + "id= "
        + getId()
        + ", studentId="
        + studentId
        + ", labProblemId="
        + labProblemId
        + ", grade="
        + grade
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Assignment that = (Assignment) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, labProblemId, grade);
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Long getLabProblemId() {
    return labProblemId;
  }

  public void setLabProblemId(Long labProblemId) {
    this.labProblemId = labProblemId;
  }

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

  @Override
  public String objectToFileLine(String delimiter) {
    return this.getId()
        + delimiter
        + this.studentId
        + delimiter
        + this.labProblemId
        + delimiter
        + grade;
  }

  @Override
  public Node objectToXMLNode(Document document) {
    Element assignmentElement = document.createElement("assignment");
    assignmentElement.setAttribute("Id", this.getId().toString());
    appendChildWithTextToNode(
        document, assignmentElement, "studentId", Long.toString(this.studentId));
    appendChildWithTextToNode(
        document, assignmentElement, "labProblemId", Long.toString(this.labProblemId));
    appendChildWithTextToNode(document, assignmentElement, "grade", Integer.toString(this.grade));
    return assignmentElement;
  }

  private void appendChildWithTextToNode(
      Document document, Node parentNode, String tagName, String textContent) {

    Element element = document.createElement(tagName);
    element.setTextContent(textContent);
    parentNode.appendChild(element);
  }
}
