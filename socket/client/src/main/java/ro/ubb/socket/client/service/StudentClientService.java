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

public class StudentClientService implements StudentService {
  private ExecutorService executorService;
  private TCPClient tcpClient;
  //private Validator<Student> validator;
  public StudentClientService(ExecutorService executorService, TCPClient tcpClient) {
    this.executorService = executorService;
    this.tcpClient = tcpClient;
    //this.validator = validator;
  }

  @Override
  public Future<Student> addStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);


      return executorService.submit(()->{
        Message request = new Message(MessageHeader.STUDENT_ADD,newStudent.objectToFileLine());
        Message response = tcpClient.sendAndReceive(request);
        if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
          throw new BadRequestException("Addition failed, entity already in repository");

          return StringEntityFactory.studentFromMessageLine(response.getBody());

        }
      );
  }

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

  @Override
  public Future<List<Student>> getAllStudentsSorted(Sort sort) {

    return executorService.submit(()->{

          String sortOrder = sort.getSortingChain().stream().map(component -> component.getKey().name() + " " + component.getValue()).
                  reduce("",(partialString, string) -> partialString + string + System.lineSeparator());
          Message request = new Message(MessageHeader.STUDENT_SORTED,sortOrder);
          Message response = tcpClient.sendAndReceive(request);
          if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
            throw new BadRequestException(response.getBody());

          String[] lines = response.getBody().split(System.lineSeparator());
          return Arrays.stream(lines).map(StringEntityFactory::studentFromMessageLine).collect(Collectors.toList());

        }
      );
    }

  @Override
  public Future<Student> getStudentById(Long id) {
    return executorService.submit(()->{
        Message request = new Message(MessageHeader.STUDENT_BY_ID,id.toString());
        Message response = tcpClient.sendAndReceive(request);
        if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
          throw new BadRequestException(response.getBody());
        return StringEntityFactory.studentFromMessageLine(response.getBody());
    });
  }

  @Override
  public Future<Student> updateStudent(Long id, String serialNumber, String name, int group)
      throws ValidatorException {
    Student newStudent = new Student(serialNumber, name, group);
    newStudent.setId(id);

    return executorService.submit(()->{
        Message request = new Message(MessageHeader.STUDENT_UPDATE,newStudent.objectToFileLine());
        Message response = tcpClient.sendAndReceive(request);
        if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
          throw new BadRequestException(response.getBody());
        return null;
    });
  }

  @Override
  public Optional<Student> deleteStudent(Long id){ // this is not needed here but necessary for interface contract
    return Optional.empty();
  }
}
