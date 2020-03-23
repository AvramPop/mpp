package ro.ubb.repository.file;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.ObjectFromFileLine;
import ro.ubb.domain.Student;

import java.util.Arrays;
import java.util.List;

/**
 * A factory creating lambda functions for creating a new entity from a file line with a given
 * delimiter
 */
public class FileLineEntityFactory {
  public static ObjectFromFileLine<LabProblem> labProblemFromFileLine() {
    return (line, delimiter) -> {
      List<String> params = Arrays.asList(line.split(delimiter));
      LabProblem labProblem = new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
      labProblem.setId(Long.parseLong(params.get(0)));
      return labProblem;
    };
  }

  public static ObjectFromFileLine<Student> studentFromFileLine() {
    return (line, delimiter) -> {
      List<String> params = Arrays.asList(line.split(delimiter));
      Student student = new Student(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
      student.setId(Long.parseLong(params.get(0)));
      return student;
    };
  }

  public static ObjectFromFileLine<Assignment> assignmentObjectFromFileLine() {
    return (line, delimiter) -> {
      List<String> params = Arrays.asList(line.split(delimiter));
      Assignment assignment =
          new Assignment(
              Long.parseLong(params.get(1)),
              Long.parseLong(params.get(2)),
              Integer.parseInt(params.get(3)));
      assignment.setId(Long.parseLong(params.get(0)));
      return assignment;
    };
  }
}
