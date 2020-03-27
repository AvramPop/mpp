package ro.ubb.socket.common.infrastructure;

import ro.ubb.socket.common.domain.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StringEntityFactory {
  public static <T extends BaseEntity<Long>> String collectionToMessageBody(Collection<T> collectionToConvert){
    return collectionToConvert.stream()
        .map(BaseEntity::objectToFileLine)
        .collect(Collectors.joining("\n"));
  }

  public static <T extends BaseEntity<Long>> String entityToMessage(T entity){
    return entity.objectToFileLine();
  }

  public static Student studentFromMessageLine(String messageLine){
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

  public LabProblem labProblemFromMessageLine(String messageLine){
    List<String> params = Arrays.asList(messageLine.split(","));
    LabProblem labProblem = new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
    labProblem.setId(Long.parseLong(params.get(0)));
    return labProblem;
  }
}
