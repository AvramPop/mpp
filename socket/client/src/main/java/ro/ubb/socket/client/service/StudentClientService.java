package ro.ubb.socket.client.service;

import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.BadRequestException;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
/**
 * Client service to handle Student specific data communicated via socket.
 */
public class StudentClientService implements StudentService {
  private ExecutorService executorService;
  private TCPClient tcpClient;
  // private Validator<Student> validator;
  public StudentClientService(ExecutorService executorService, TCPClient tcpClient) {
    this.executorService = executorService;
    this.tcpClient = tcpClient;
    // this.validator = validator;
  }
  /**
   * Make server-call to add Student with data specified in params.

   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.STUDENT_ADD, newStudent.objectToFileLine());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException("Addition failed, entity already in repository");

          return null;
        });
  }
  /**
   * Make server-call to get all Students.

   * @return Future containing a Set of all Students.
   * @throws Exception if call fails
   */
  @Override
  public Future<Set<Student>> getAllStudents() {
    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.STUDENT_ALL, "");
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());

          String[] lines = response.getBody().split(System.lineSeparator());
          return Arrays.stream(lines)
              .map(StringEntityFactory::studentFromMessageLine)
              .collect(Collectors.toSet());
        });
  }
  /**
   * Make server-call to get all Students sorted by given Sort.
   *
   * @return Future containing the Students sorted by Sort criteria
   * @throws Exception if call fails
   */
  @Override
  public Future<List<Student>> getAllStudentsSorted(Sort sort) {

    return executorService.submit(
        () -> {
          String sortOrder =
              sort.getSortingChain().stream()
                  .map(component -> component.getKey().name() + " " + component.getValue())
                  .reduce(
                      "",
                      (partialString, string) -> partialString + string + System.lineSeparator());
          Message request = new Message(MessageHeader.STUDENT_SORTED, sortOrder);
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());

          String[] lines = response.getBody().split(System.lineSeparator());
          return Arrays.stream(lines)
              .map(StringEntityFactory::studentFromMessageLine)
              .collect(Collectors.toList());
        });
  }
  /**
   * Make server-call to get Student specified by given id.

   * @return Future containing the sought Student.
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Student> getStudentById(Long id) {
    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.STUDENT_BY_ID, id.toString());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return StringEntityFactory.studentFromMessageLine(response.getBody());
        });
  }
  /**
   * Make server-call to update Student with given data.

   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    return executorService.submit(
        () -> {
          Message request =
              new Message(MessageHeader.STUDENT_UPDATE, newStudent.objectToFileLine());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return true;
        });
  }
  /**
   * Make server-call to delete Student specified by given id.

   * @return Future containing the truth value of the success of the operation
   * @throws Exception if data given is invalid or call fails
   */
  @Override
  public Future<Boolean> deleteStudent(Long id) {

    return executorService.submit(
        () -> {
          Message request = new Message(MessageHeader.STUDENT_DELETE, id.toString());
          Message response = tcpClient.sendAndReceive(request);
          if (response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());
          return true;
        });
  }
}
