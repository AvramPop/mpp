package ro.ubb.socket.server.infrastructure;

import jdk.swing.interop.SwingInterOpUtils;
import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HandlerManager {
  private TCPServer server;
  private StudentService studentService;
  private LabProblemService labProblemService;
  private AssignmentService assignmentService;

  public HandlerManager(TCPServer server, StudentService studentService, LabProblemService labProblemService, AssignmentService assignmentService){
    this.server = server;
    this.studentService = studentService;
    this.labProblemService = labProblemService;
    this.assignmentService = assignmentService;
  }

  public void addHandlers(){
    studentByIdHandler();
    allStudentsHandler();
    allStudentsSortedHandler();
    addStudentHandler();
    updateStudentHandler();
    deleteStudentHandler();

    labProblemByIdHandler();
    allLabProblemsHandler();
    allLabProblemsSortedHandler();
    addLabProblemHandler();
    updateLabProblemHandler();
    deleteLabProblemHandler();

    assignmentByIdHandler();
    allAssignmentsHandler();
    allAssignmentsSortedHandler();
    addAssignmentsHandler();
    updateAssignmentHandler();
    deleteAssignmentHandler();

    greatestMeanHandler();
    idOfLabProblemMostAssignedHandler();
    averageGradeHandler();
    studentAssignedProblemsHandler();

    serverShutdownHandler();
  }

  private void serverShutdownHandler(){
    server.addHandler(
        MessageHeader.SERVER_SHUTDOWN,
        (request) -> new Message(
        MessageHeader.OK_REQUEST, ""));
  }

  private void studentAssignedProblemsHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_PROBLEM_MAPPING,
        (request) -> {
          Future<Map<Student, List<LabProblem>>> future =
              assignmentService.studentAssignedProblems();
          try {
            Map<Student, List<LabProblem>> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.mapToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void averageGradeHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_AVERAGE_GRADE,
        (request) -> {
          Future<Double> future =
              assignmentService.averageGrade();
          try {
            Double result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.simpleValueToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void idOfLabProblemMostAssignedHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_PROBLEM_MOST_ASSIGNED,
        (request) -> {
          Future<AbstractMap.SimpleEntry<Long, Long>> future =
              assignmentService.idOfLabProblemMostAssigned();
          try {
            AbstractMap.SimpleEntry<Long, Long> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.pairToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void greatestMeanHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_GREATEST_MEAN,
        (request) -> {
          Future<AbstractMap.SimpleEntry<Long, Double>> future =
              assignmentService.greatestMean();
          try {
            AbstractMap.SimpleEntry<Long, Double> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.pairToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void deleteAssignmentHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_DELETE,
        (request) -> {
          try{
            if(assignmentService.deleteAssignment(Long.parseLong(request.getBody())).get()){
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void updateAssignmentHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_UPDATE,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");
          System.out.println("-*----------------");
          for (String s : parsedRequest) {
            System.out.println(s);
          }
          System.out.println("---------------");
          try{
            if (assignmentService
                .updateAssignment(
                    Long.parseLong(parsedRequest[0]),
                    Long.parseLong(parsedRequest[1]),
                    Long.parseLong(parsedRequest[2]),
                    Integer.parseInt(parsedRequest[3]))
                .get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void addAssignmentsHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_ADD,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");
          try{
            if(assignmentService.addAssignment(
                Long.parseLong(parsedRequest[0]),
                Long.parseLong(parsedRequest[1]),
                Long.parseLong(parsedRequest[2]),
                Integer.parseInt(parsedRequest[3])).get()){

              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void allAssignmentsSortedHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_SORTED,
        (request) -> {
          Future<List<Assignment>> future = assignmentService.getAllAssignmentsSorted(getSortFromRequestBody(request, "Assignment"));
          try {
            List<Assignment> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void allAssignmentsHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_ALL,
        (request) -> {
          Future<Set<Assignment>> future = assignmentService.getAllAssignments();
          try {
            Set<Assignment> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void assignmentByIdHandler(){
    server.addHandler(
        MessageHeader.ASSIGNMENT_BY_ID,
        (request) -> {
          Future<Assignment> future =
              assignmentService.getAssignmentById(Long.parseLong(request.getBody()));
          try {
            Assignment result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.entityToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void deleteLabProblemHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_DELETE,
        (request) -> {
          try {
            if (assignmentService.deleteLabProblem(Long.parseLong(request.getBody())).get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
          return null;
        });
  }

  private void updateLabProblemHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_UPDATE,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");
          try{
            if (labProblemService
                .updateLabProblem(
                    Long.parseLong(parsedRequest[0]),
                    Integer.parseInt(parsedRequest[1]),
                    parsedRequest[2])
                .get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void addLabProblemHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_ADD,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");
          try{
            if (labProblemService
                .addLabProblem(
                    Long.parseLong(parsedRequest[0]),
                    Integer.parseInt(parsedRequest[1]),
                    parsedRequest[2])
                .get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void allLabProblemsSortedHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_SORTED,
        (request) -> {
          Future<List<LabProblem>> future = labProblemService.getAllLabProblemsSorted(getSortFromRequestBody(request, "LabProblem"));
          try {
            List<LabProblem> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private Sort getSortFromRequestBody(Message request, String className){
    var wrapper = new Object() {
      Sort sort = null;};
    request.getBody()
        .lines()
        .forEach(line -> {
          String temp[] = line.split(" ");
          if(wrapper.sort == null){
            Sort tempSort = new Sort(temp[0], temp[1]);
            tempSort.setClassName(className);
            wrapper.sort = tempSort;
          } else {
            wrapper.sort.and(new Sort(temp[0], temp[1]));
          }
        });
    return wrapper.sort;
  }

  private void allLabProblemsHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_ALL,
        (request) -> {
          Future<Set<LabProblem>> future = labProblemService.getAllLabProblems();
          try {
            Set<LabProblem> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void labProblemByIdHandler(){
    server.addHandler(
        MessageHeader.LABPROBLEM_BY_ID,
        (request) -> {
          Future<LabProblem> future =
              labProblemService.getLabProblemById(Long.parseLong(request.getBody()));
          try {
            LabProblem result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.entityToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void allStudentsSortedHandler(){
    server.addHandler(
        MessageHeader.STUDENT_SORTED,
        (request) -> {
          Future<List<Student>> future = studentService.getAllStudentsSorted(getSortFromRequestBody(request, "Student"));
          try {
            List<Student> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void deleteStudentHandler(){
    server.addHandler(
        MessageHeader.STUDENT_DELETE,
        (request) -> {
          try {
            if (assignmentService.deleteStudent(Long.parseLong(request.getBody())).get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
          return null;
        });
  }

  private void updateStudentHandler(){
    server.addHandler(
        MessageHeader.STUDENT_UPDATE,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");

          try {
            if (studentService
                .updateStudent(
                    Long.parseLong(parsedRequest[0]),
                    parsedRequest[1],
                    parsedRequest[2],
                    Integer.parseInt(parsedRequest[3]))
                .get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
          return null;
        });
  }

  private void addStudentHandler(){
    server.addHandler(
        MessageHeader.STUDENT_ADD,
        (request) -> {
          String[] parsedRequest = request.getBody().split(", ");
          try{
            if (studentService
                .addStudent(
                    Long.parseLong(parsedRequest[0]),
                    parsedRequest[1],
                    parsedRequest[2],
                    Integer.parseInt(parsedRequest[3]))
                .get()) {
              return new Message(MessageHeader.OK_REQUEST, "");
            } else {
              return new Message(MessageHeader.BAD_REQUEST, "");
            }
          } catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
          }
          return null;
        });
  }

  private void allStudentsHandler(){
    server.addHandler(
        MessageHeader.STUDENT_ALL,
        (request) -> {
          Future<Set<Student>> future = studentService.getAllStudents();
          try {
            Set<Student> result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.collectionToMessageBody(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }

  private void studentByIdHandler(){
    server.addHandler(
        MessageHeader.STUDENT_BY_ID,
        (request) -> {
          Future<Student> future =
              studentService.getStudentById(Long.parseLong(request.getBody()));
          try {
            Student result = future.get();
            return new Message(
                MessageHeader.OK_REQUEST, StringEntityFactory.entityToMessage(result));
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
          }
        });
  }


}
