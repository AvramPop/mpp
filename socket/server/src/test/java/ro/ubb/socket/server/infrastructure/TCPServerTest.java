package ro.ubb.socket.server.infrastructure;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ServiceException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.LabProblemService;
import ro.ubb.socket.common.service.StudentService;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class TCPServerTest {

  private Socket clientSocket;
  private InputStream clientInputStream;
  private OutputStream clientOutputStream;
  private Message serverResponse;
  private TCPServer tcpServer;
  private ExecutorService executorService;
  @Before
  public void setUp() throws Exception {
    Validator<Student> studentValidator = new StudentValidator();
    DBStudentRepository studentRepository =
        new DBStudentRepository(
            ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data", "public.\"Students_test\"");
    Validator<LabProblem> labProblemValidator = new LabProblemValidator();
    DBLabProblemRepository labProblemRepository =
        new DBLabProblemRepository(
            ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data",  "public.\"LabProblems_test\"");
    Validator<Assignment> assignmentValidator = new AssignmentValidator();
    DBAssignmentsRepository assignmentsRepository =
        new DBAssignmentsRepository(
            ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data", "public.\"Assignments_test\"");
    executorService =
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
    tcpServer = new TCPServer(executorService, Message.TEST_PORT);
    HandlerManager handlerManager = new HandlerManager(tcpServer, studentService, labProblemService, assignmentService);
    handlerManager.addHandlers();
    Thread serverThread = new Thread(() -> tcpServer.startServer());
    serverThread.start();
    clientSocket = new Socket(Message.HOST, Message.TEST_PORT);
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    serverResponse = new Message();


    }



  @After
  public void tearDown() throws Exception {
    Message clientRequest = new Message(MessageHeader.SERVER_SHUTDOWN, "");
    clientRequest.writeTo(clientOutputStream);
    executorService.shutdown();
    executorService = null;
    tcpServer = null;
    clientSocket = null;
    clientInputStream = null;
    clientOutputStream = null;
    serverResponse = null;
  }

  @Test
  public void getLabProblems() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_ALL, "");
    clientRequest.writeTo(clientOutputStream);
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals("1,1,aaa", serverResponse.getBody());
  }
}