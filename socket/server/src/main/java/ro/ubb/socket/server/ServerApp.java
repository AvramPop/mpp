package ro.ubb.socket.server;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.infrastructure.StringEntityFactory;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.StudentService;
import ro.ubb.socket.server.infrastructure.TCPServer;
import ro.ubb.socket.server.repository.db.DBAssignmentsRepository;
import ro.ubb.socket.server.repository.db.DBLabProblemRepository;
import ro.ubb.socket.server.repository.db.DBStudentRepository;
import ro.ubb.socket.server.service.AssignmentServerService;
import ro.ubb.socket.server.service.LabProblemServerService;
import ro.ubb.socket.server.service.StudentServerService;
import ro.ubb.socket.server.service.validators.AssignmentValidator;
import ro.ubb.socket.server.service.validators.LabProblemValidator;
import ro.ubb.socket.server.service.validators.StudentValidator;
import ro.ubb.socket.server.service.validators.Validator;

import java.nio.file.FileSystems;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {

  public static void main(String[] args) {
    Validator<Student> studentValidator = new StudentValidator();
    DBStudentRepository studentRepository =
        new DBStudentRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data");
    Validator<LabProblem> labProblemValidator = new LabProblemValidator();
    DBLabProblemRepository labProblemRepository =
        new DBLabProblemRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data");
    Validator<Assignment> assignmentValidator = new AssignmentValidator();
    DBAssignmentsRepository assignmentsRepository =
        new DBAssignmentsRepository(
            "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data");
    try {
      System.out.println("server started");
      ExecutorService executorService =
          Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
      StudentService studentService =
          new StudentServerService(studentRepository, studentValidator, executorService);
      LabProblemService labProblemService =
          new LabProblemServerService(labProblemRepository, labProblemValidator, executorService);
      AssignmentService assignmentService =
          new AssignmentServerService(
              assignmentsRepository,
              assignmentValidator,
              executorService,
              labProblemService,
              studentService);
      TCPServer tcpServer = new TCPServer(executorService);

      tcpServer.addHandler(
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

      tcpServer.addHandler(
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

      tcpServer.addHandler(
          MessageHeader.STUDENT_ADD,
          (request) -> {
            String[] parsedRequest = request.getBody().split(", ");
            for (int i = 0; i < parsedRequest.length; i++) {
              System.out.println(parsedRequest[i]);
            }
            studentService.addStudent(
                Long.parseLong(parsedRequest[0]),
                parsedRequest[1],
                parsedRequest[2],
                Integer.parseInt(parsedRequest[3]));
            return new Message(MessageHeader.OK_REQUEST, "");
          });

      tcpServer.addHandler(
          MessageHeader.STUDENT_UPDATE,
          (request) -> {
            String[] parsedRequest = request.getBody().split(", ");
            for (int i = 0; i < parsedRequest.length; i++) {
              System.out.println(parsedRequest[i]);
            }
            studentService.updateStudent(
                Long.parseLong(parsedRequest[0]),
                parsedRequest[1],
                parsedRequest[2],
                Integer.parseInt(parsedRequest[3]));
            return new Message(MessageHeader.OK_REQUEST, "");
          });

      tcpServer.addHandler(
          MessageHeader.STUDENT_DELETE,
          (request) -> {
            Future<Student> future =
                assignmentService.deleteStudent(Long.parseLong(request.getBody()));
            try {
              Student result = future.get();
              return new Message(MessageHeader.OK_REQUEST, result.objectToFileLine());
            } catch (InterruptedException | ExecutionException e) {
              e.printStackTrace();
              return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
            }
          });
      //
      //            tcpServer.addHandler(MessageHeader.STUDENT_ALL, (request) -> {
      //                Future<Set<Student>> future = studentService.getAllStudents();
      //                try {
      //                    Set<Student> result = future.get();
      //                    return new Message(MessageHeader.OK_REQUEST,
      // StringEntityFactory.collectionToMessageBody(result));
      //                } catch (InterruptedException | ExecutionException e) {
      //                    e.printStackTrace();
      //                    return new Message(MessageHeader.BAD_REQUEST, e.getMessage());
      //                }
      //
      //            });

      tcpServer.startServer();

      executorService.shutdown();
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    }
  }
}
