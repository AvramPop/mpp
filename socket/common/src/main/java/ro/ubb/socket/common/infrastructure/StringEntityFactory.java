package ro.ubb.socket.common.infrastructure;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.BaseEntity;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;

import java.util.*;
import java.util.stream.Collectors;

public class StringEntityFactory {
  /**
   * Converts a given collection into a string form for the message body
   *
   * @param collectionToConvert the collection to convert
   * @param <T> the type of the collection
   * @return the result string
   */
  public static <T extends BaseEntity<Long>> String collectionToMessageBody(
      Collection<T> collectionToConvert) {
    return collectionToConvert.stream()
        .map(BaseEntity::objectToFileLine)
        .collect(Collectors.joining("\n"));
  }

  /**
   * Converts a given entity into a string
   *
   * @param entity the entity
   * @param <T> the type of the entity
   * @return the string form
   */
  public static <T extends BaseEntity<Long>> String entityToMessage(T entity) {
    return entity.objectToFileLine();
  }

  /**
   * Converts from a message line a student
   *
   * @param messageLine the string
   * @return the resulting entity
   */
  public static Student studentFromMessageLine(String messageLine) {
    List<String> params = Arrays.asList(messageLine.split(","));
    Student student = new Student(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
    student.setId(Long.parseLong(params.get(0)));
    return student;
  }
  /**
   * Converts from a message line a lab assignment
   *
   * @param messageLine the string
   * @return the resulting entity
   */
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
  /**
   * Converts an AbstractMap.SimpleEntry entity pair into a string line
   *
   * @param pair the entity to convert
   * @param <K> the type of the first entity
   * @param <V> the type of the second entity
   * @return the result string
   */
  public static <K, V> String pairToMessage(AbstractMap.SimpleEntry<K, V> pair) {
    return pair.getKey().toString() + "," + pair.getValue().toString();
  }
  /**
   * Converts an entity into a string line
   *
   * @param value the entity to convert
   * @param <T> the type of the entity
   * @return the result string
   */
  public static <T> String simpleValueToMessage(T value) {
    return value.toString();
  }
  /**
   * Converts a given map into a string form for the message body
   *
   * @param map the map to convert
   * @param <T> the type of the collection
   * @return the result string
   */
  public static <T extends BaseEntity<Long>, K extends BaseEntity<Long>> String mapToMessage(
      Map<T, List<K>> map) {
    String result =
        map.entrySet().stream()
            .map(
                entry ->
                    entry.getKey().objectToFileLine()
                        + "?"
                        + collectionToLine(entry.getValue())
                        + System.lineSeparator())
            .reduce("", String::concat);
    result =
        result.substring(
            0, result.length() - System.lineSeparator().length()); // remove last newline
    return result;
  }
  /**
   * Converts a given list into a string line
   *
   * @param list the collection to convert
   * @param <T> the type of the collection
   * @return the result string
   */
  private static <T extends BaseEntity<Long>> String collectionToLine(List<T> list) {
    return list.stream()
        .map(problem -> problem.objectToFileLine() + ";")
        .reduce("", String::concat)
        .replaceFirst(".$", ""); // removes last semicolon
  }
  /**
   * Converts from a message line a lab problem
   *
   * @param messageLine the string
   * @return the resulting entity
   */
  public static LabProblem labProblemFromMessageLine(String messageLine) {
    List<String> params = Arrays.asList(messageLine.split(","));
    LabProblem labProblem = new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
    labProblem.setId(Long.parseLong(params.get(0)));
    return labProblem;
  }
}
