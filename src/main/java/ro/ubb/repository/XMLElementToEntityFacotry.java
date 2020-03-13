package ro.ubb.repository;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ro.ubb.domain.*;

import java.util.Arrays;
import java.util.List;

public class XMLElementToEntityFacotry {

  public static ObjectFromXMLFile<Assignment> assignmentObjectFromXMLFile() {
    return (element) -> {
      Assignment assignment = new Assignment();

      Long Id = Long.parseLong(element.getAttribute("Id"));
      assignment.setId(Id);

      assignment.setLabProblemId(Long.parseLong(getTextFromTagName(element, "labProblemId")));
      assignment.setStudentId(Long.parseLong(getTextFromTagName(element, "studentId")));
      assignment.setGrade(Integer.parseInt(getTextFromTagName(element, "grade")));

      return assignment;
    };
  }

  public static ObjectFromXMLFile<Student> studentObjectFromXMLFile() {
    return (element) -> {
      Student student = new Student();

      Long Id = Long.parseLong(element.getAttribute("Id"));
      student.setId(Id);

      student.setName(getTextFromTagName(element, "name"));
      student.setSerialNumber(getTextFromTagName(element, "serialNumber"));
      student.setGroup(Integer.parseInt(getTextFromTagName(element, "group")));

      return student;
    };
  }

  public static ObjectFromXMLFile<LabProblem> labProblemObjectFromXMLFile() {
    return (element) -> {
      LabProblem labProblem = new LabProblem();

      Long Id = Long.parseLong(element.getAttribute("Id"));
      labProblem.setId(Id);

      labProblem.setDescription(getTextFromTagName(element, "description"));
      labProblem.setProblemNumber(Integer.parseInt(getTextFromTagName(element, "problemNumber")));

      return labProblem;
    };
  }

  private static String getTextFromTagName(Element element, String tagName) {
    Node node = element.getElementsByTagName(tagName).item(0);
    return node.getTextContent();
  }
}
