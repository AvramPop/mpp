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
import ro.ubb.socket.server.infrastructure.HandlerManager;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {

  public static void main(String[] args) {
//    Student s = new Student();
//    System.out.println(s.getClass().getCanonicalName());
//    Class<?> clazz = null;
//    try{
//      clazz = Class.forName("common.ro.ubb.socket.common.domain.Student");
//      Constructor<?> ctor = clazz.getConstructor(String.class, String.class, Integer.class);
//      Object object = ctor.newInstance("a", "a", 1);
//      System.out.println(clazz.toString());
//    } catch(ClassNotFoundException e){
//      System.err.println("nume gresit de clasa");
//    } catch( NoSuchMethodException e){
//      System.err.println("1");
//    } catch( IllegalAccessException e){
//      System.err.println("2");
//    } catch( InstantiationException e){
//      System.err.println("3");
//    } catch( InvocationTargetException e){
//      System.err.println("4");
//    }
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
    try {
      TCPServer tcpServer = new TCPServer(executorService);
      HandlerManager handlerManager = new HandlerManager(tcpServer, studentService, labProblemService, assignmentService);
      handlerManager.addHandlers();
      tcpServer.startServer();
      executorService.shutdown();
    } catch (RuntimeException ex) {
      ex.printStackTrace();
    }
  }
}
