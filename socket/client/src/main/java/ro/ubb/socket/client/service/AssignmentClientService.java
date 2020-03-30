package ro.ubb.socket.client.service;

import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.BadRequestException;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/** Client service to handle Assignment specific data communicated via socket. */
public class AssignmentClientService implements AssignmentService {

  private ExecutorService executorService;
  private TCPClient tcpClient;
  // private Validator<Assignment> validator;
  public AssignmentClientService(ExecutorService executorService, TCPClient tcpClient) {
    this.executorService = executorService;
    this.tcpClient = tcpClient;
    // this.validator = validator;
  }

  /**
   * Make server-call to add Assignment with data specified in params.
   *
   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);

    return executorService.submit(
        () -> {
          Message request =
              new Message(MessageHeader.ASSIGNMENT_ADD, newAssignment.objectToFileLine());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException("Addition failed, entity already in repository");

          return true;
        });
  }

  /**
   * Make server-call to get all Assignments.
   *
   * @return Future containing a Set of all Assignments.
   * @throws Exception if call fails
   */
  @Override
  public Future<Set<Assignment>> getAllAssignments() {
    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_ALL, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());

          String[] lines = response.getBody().split(System.lineSeparator());
          return Arrays.stream(lines)
              .map(StringEntityFactory::assignmentFromMessageLine)
              .collect(Collectors.toSet());
        });
  }

  /**
   * Make server-call to get all Assignments sorted by given Sort.
   *
   * @return Future containing the Assignments sorted by Sort criteria
   * @throws Exception if call fails
   */
  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort) {

    return executorService.submit(
        () -> {
          String sortOrder =
              sort.getSortingChain().stream()
                  .map(component -> component.getKey().name() + " " + component.getValue())
                  .reduce(
                      "",
                      (partialString, string) -> partialString + string + System.lineSeparator());
          Message request = new Message(MessageHeader.ASSIGNMENT_SORTED, sortOrder);
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());

          String[] lines = response.getBody().split(System.lineSeparator());
          return Arrays.stream(lines)
              .map(StringEntityFactory::assignmentFromMessageLine)
              .collect(Collectors.toList());
        });
  }

  /**
   * Make server-call to get Assignment specified by given id.
   *
   * @return Future containing the sought Assignment.
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Assignment> getAssignmentById(Long id) {

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_BY_ID, id.toString());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return StringEntityFactory.assignmentFromMessageLine(response.getBody());
        });
  }

  /**
   * Make server-call to delete Assignment specified by given id.
   *
   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> deleteAssignment(Long id) {

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_DELETE, id.toString());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return true;
        });
  }
  /**
   * Make server-call to update Assignment with given data.
   *
   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);

    return executorService.submit(
        () -> {
          Message request =
              new Message(MessageHeader.ASSIGNMENT_UPDATE, newAssignment.objectToFileLine());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return true;
        });
  }

  /**
   * Make server-call to get the Student with the greatest mean and his mean.
   *
   * @return Future containing a Pair between the id of the student and his mean.
   * @throws Exception if call fails
   */
  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_GREATEST_MEAN, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          String[] params = response.getBody().split(",");
          return new AbstractMap.SimpleEntry<>(
              Long.parseLong(params[0]), Double.parseDouble(params[1]));
        });
  }

  /**
   * Make server-call to get the id of the problem most assigned and the times it got assigned.
   *
   * @return Future containing a Pair between the id of the problem and times assigned.
   * @throws Exception if call fails
   */
  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MOST_ASSIGNED, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          String[] params = response.getBody().split(",");
          return new AbstractMap.SimpleEntry<>(
              Long.parseLong(params[0]), Long.parseLong(params[1]));
        });
  }

  /**
   * Make server-call to get the average grade of all assignments.
   *
   * @return Future containing the average grade
   * @throws Exception if call fails
   */
  @Override
  public Future<Double> averageGrade() {

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_AVERAGE_GRADE, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return Double.parseDouble(response.getBody());
        });
  }
  /**
   * Make server-call to get a map between each student and the problems assigned to him.
   *
   * @return Future containing the sought map
   * @throws Exception if call fails
   */
  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    // fixme: java 8 with streams, maybe using different representation
    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MAPPING, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          List<String> lines =
              Arrays.stream(response.getBody().split(System.lineSeparator()))
                  .collect(Collectors.toList());

          return lines.stream()
              .collect(
                  Collectors.toMap(
                      elem -> StringEntityFactory.studentFromMessageLine(elem.split("\\?")[0]),
                          this::convertLineToLabProblemList));
        });
  }

  private List<LabProblem> convertLineToLabProblemList(String line) {
    try {
      return Arrays.stream(line.split("\\?")[1].split(";"))
          .map(StringEntityFactory::labProblemFromMessageLine)
          .collect(Collectors.toList());
    } catch (ArrayIndexOutOfBoundsException ex) {
      return new LinkedList<>();
    }
  }

  /** Shutdown server call. */
  public void shutDownServer() {
    tcpClient.sendAndReceive(new Message(MessageHeader.SERVER_SHUTDOWN, ""));
  }
}
