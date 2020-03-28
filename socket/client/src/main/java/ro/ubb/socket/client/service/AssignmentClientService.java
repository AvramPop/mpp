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

public class AssignmentClientService implements AssignmentService {

  private ExecutorService executorService;
  private TCPClient tcpClient;
  //private Validator<Assignment> validator;
  public AssignmentClientService(ExecutorService executorService, TCPClient tcpClient) {
    this.executorService = executorService;
    this.tcpClient = tcpClient;
    //this.validator = validator;
  }


  @Override
  public Future<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID, grade);
    newAssignment.setId(id);


    return executorService.submit(()->{
              Message request = new Message(MessageHeader.ASSIGNMENT_ADD,newAssignment.objectToFileLine());
              Message response = tcpClient.sendAndReceive(request);
              if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
                throw new BadRequestException("Addition failed, entity already in repository");

              return StringEntityFactory.assignmentFromMessageLine(response.getBody());

            }
    );
  }

  @Override
  public Future<Set<Assignment>> getAllAssignments() {
    return executorService.submit(()->{
              Message request = new Message(MessageHeader.ASSIGNMENT_ALL,"");
              Message response = tcpClient.sendAndReceive(request);
              if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
                throw new BadRequestException(response.getBody());

              String[] lines = response.getBody().split(System.lineSeparator());
              return Arrays.stream(lines).map(StringEntityFactory::assignmentFromMessageLine).collect(Collectors.toSet());

            }
    );


  }

  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort) {

    return executorService.submit(()->{

              String sortOrder = sort.getSortingChain().stream().map(component -> component.getKey().name() + " " + component.getValue()).
                      reduce("",(partialString, string) -> partialString + string + System.lineSeparator());
              Message request = new Message(MessageHeader.ASSIGNMENT_SORTED,sortOrder);
              Message response = tcpClient.sendAndReceive(request);
              if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
                throw new BadRequestException(response.getBody());

              String[] lines = response.getBody().split(System.lineSeparator());
              return Arrays.stream(lines).map(StringEntityFactory::assignmentFromMessageLine).collect(Collectors.toList());

            }
    );


  }

  @Override
  public Future<Assignment> getAssignmentById(Long id) {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_BY_ID,id.toString());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return StringEntityFactory.assignmentFromMessageLine(response.getBody());
    });

  }

  @Override
  public Future<Assignment> deleteAssignment(Long id) {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_DELETE,id.toString());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return StringEntityFactory.assignmentFromMessageLine(response.getBody());
    });
  }

  @Override
  public Future<Student> deleteStudent(Long id) {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.STUDENT_DELETE,id.toString());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return StringEntityFactory.studentFromMessageLine(response.getBody());
    });

  }

  @Override
  public Future<LabProblem> deleteLabProblem(Long id) {
    return executorService.submit(()->{
      Message request = new Message(MessageHeader.LABPROBLEM_DELETE,id.toString());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return StringEntityFactory.labProblemFromMessageLine(response.getBody());
    });

  }

  @Override
  public Future<Assignment> updateAssignment(Long id, Long studentID, Long labProblemID, int grade)
      throws ValidatorException {
    Assignment newAssignment = new Assignment(studentID, labProblemID,grade);
    newAssignment.setId(id);

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_UPDATE,newAssignment.objectToFileLine());
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return null;
    });
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean() {
    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_GREATEST_MEAN,"");
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
        String[] params = response.getBody().split(",");
        return new AbstractMap.SimpleEntry<Long, Double>(Long.parseLong(params[0]),Double.parseDouble(params[1]));
    });


  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned() {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MOST_ASSIGNED,"");
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      String[] params = response.getBody().split(",");
      return new AbstractMap.SimpleEntry<Long, Long>(Long.parseLong(params[0]),Long.parseLong(params[1]));
    });

  }

  @Override
  public Future<Double> averageGrade() {

    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_AVERAGE_GRADE,"");
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      return Double.parseDouble(response.getBody());
    });

  }

  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems() {
    //fixme: java 8 with streams, maybe using different representation
    return executorService.submit(()->{
      Message request = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MAPPING,"");
      Message response = tcpClient.sendAndReceive(request);
      if(response.getHeader().equals(MessageHeader.BAD_REQUEST))
        throw new BadRequestException(response.getBody());
      String[] lines = response.getBody().split(System.lineSeparator());
      int i = 0;
      Map<Student, List<LabProblem>> studentListMap = new HashMap<>();
      while (i < lines.length){
        int lengthOfCluster = Integer.parseInt(lines[i]);
        i++;
        Student newStudent = StringEntityFactory.studentFromMessageLine(lines[i]);
        i++;
        List<LabProblem> labProblemList = new LinkedList<>();
        for(int j = 0; j < lengthOfCluster; j++,i++){
          labProblemList.add(StringEntityFactory.labProblemFromMessageLine(lines[i]));
        }
        studentListMap.put(newStudent,labProblemList);
      }
      return studentListMap;
    });

  }
}
