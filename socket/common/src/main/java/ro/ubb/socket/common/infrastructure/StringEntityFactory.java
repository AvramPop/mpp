package ro.ubb.socket.common.infrastructure;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StringEntityFactory {
  public static <T extends BaseEntity<Long>> String collectionToMessageBody(
      Collection<T> collectionToConvert) {
    return collectionToConvert.stream()
        .map(BaseEntity::objectToFileLine)
        .collect(Collectors.joining("\n"));
  }

  public static <T extends BaseEntity<Long>> String entityToMessage(T entity) {
    return entity.objectToFileLine();
  }

  public static Student studentFromMessageLine(String messageLine) {
    List<String> params = Arrays.asList(messageLine.split(","));
    Student student = new Student(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
    student.setId(Long.parseLong(params.get(0)));
    return student;
  }

  public static Assignment assignmentFromMessageLine(String messageLine) {
    List<String> params = Arrays.asList(messageLine.split(","));
    Assignment assignment =
        new Assignment(
            Long.parseLong(params.get(1)),
            Long.parseLong(params.get(2)),
            Integer.parseInt(params.get(3)));
    assignment.setId(Long.parseLong(params.get(0)));
    return assignment;
  }

  public static <K, V> String pairToMessage(AbstractMap.SimpleEntry<K, V> pair){
    return pair.getKey().toString() + ", " + pair.getValue().toString();
  }

  public static <T> String simpleValueToMessage(T value){
    return value.toString();
  }

  public static <T extends BaseEntity<Long>, K extends BaseEntity<Long>> String mapToMessage(Map<T, List<K>> map){
    String result =
        map.entrySet()
        .stream()
        .map(entry -> entry.getKey().objectToFileLine() + "?" + collectionToLine(entry.getValue()) + System.lineSeparator())
        .reduce("", String::concat);
    result = result.substring(0, result.length() - System.lineSeparator().length()); // remove last newline
    return result;
  }

  private static <T extends BaseEntity<Long>> String collectionToLine(List<T> list){
    return list
        .stream()
        .map(problem -> problem.objectToFileLine() + ";")
        .reduce("", String::concat)
        .replaceFirst(".$", ""); // removes last semicolon
  }

  public static LabProblem labProblemFromMessageLine(String messageLine) {
    List<String> params = Arrays.asList(messageLine.split(","));
    LabProblem labProblem = new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
    labProblem.setId(Long.parseLong(params.get(0)));
    return labProblem;
  }
}
