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
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class TCPServerTest {

  private Socket clientSocket;
  private TCPServer tcpServer;
  private ExecutorService executorService;
  private Student student;
  private LabProblem labProblem;
  private Assignment assignment;
  private InputStream clientInputStream;
  private OutputStream clientOutputStream;
  private DBStudentRepository studentRepository;
  private DBLabProblemRepository labProblemRepository;
  private DBAssignmentsRepository assignmentsRepository;
  private Message serverResponse;


  @Before
  public void setUp() throws Exception {
    Validator<Student> studentValidator = new StudentValidator();
    studentRepository = new DBStudentRepository(
        ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data", "public.\"Students_test\"");
    student = new Student("sn1", "studentName", 1);
    student.setId(1L);
   // studentRepository.save(student);
    Validator<LabProblem> labProblemValidator = new LabProblemValidator();

    labProblemRepository = new DBLabProblemRepository(
        ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data", "public.\"LabProblems_test\"");
    labProblem = new LabProblem(11, "description");
    labProblem.setId(1L);
//    labProblemRepository.save(labProblem);
    Validator<Assignment> assignmentValidator = new AssignmentValidator();
    assignmentsRepository = new DBAssignmentsRepository(
        ".." + FileSystems.getDefault().getSeparator() + "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data", "public.\"Assignments_test\"");
    assignment = new Assignment(1L, 1L, 10);
    assignment.setId(1L);
//    assignmentsRepository.save(assignment);
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
    labProblemRepository.save(labProblem);
    studentRepository.save(student);
    assignmentsRepository.save(assignment);
    }



  @After
  public void tearDown() throws Exception {
    clientSocket.setReuseAddress(true);
    clientSocket.close();
    clientSocket = new Socket(Message.HOST, Message.TEST_PORT);
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    assignmentsRepository.delete(assignment.getId());
    labProblemRepository.delete(labProblem.getId());
    studentRepository.delete(student.getId());
    Message clientRequest = new Message(MessageHeader.SERVER_SHUTDOWN, "");
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    executorService.shutdown();
    clientSocket.close();

    student = null;
    assignment = null;
    labProblem = null;
    assignmentsRepository = null;
    studentRepository = null;
    labProblemRepository = null;
    executorService = null;
    tcpServer = null;
    clientSocket = null;
    clientOutputStream = null;
    clientInputStream = null;
    serverResponse = null;
  }

  @Test
  public void getAllLabProblems() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_ALL, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,11,description", serverResponse.getBody());
  }

  @Test
  public void getLabProblemById() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_BY_ID, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,11,description", serverResponse.getBody());
  }

  @Test
  public void getAllStudents() throws IOException{
    Message clientRequest = new Message(MessageHeader.STUDENT_ALL, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,sn1,studentName,1", serverResponse.getBody());
  }

  @Test
  public void getStudentById() throws IOException{
    Message clientRequest = new Message(MessageHeader.STUDENT_BY_ID, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,sn1,studentName,1", serverResponse.getBody());
  }

  @Test
  public void getAllAssignments() throws IOException{
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_ALL, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,1,1,10", serverResponse.getBody());
  }

  @Test
  public void getAssignmentById() throws IOException{
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_BY_ID, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,1,1,10", serverResponse.getBody());
  }

  @Test
  public void addStudent() throws IOException{
    Message clientRequest = new Message(MessageHeader.STUDENT_ADD, "2, sn2, name2, 2");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(studentRepository.findAll().spliterator(), false).count(), 2L);
    studentRepository.delete(2L);
  }

  @Test
  public void updateStudent() throws IOException{
    Message clientRequest = new Message(MessageHeader.STUDENT_UPDATE, "1, sn1up, name1up, 1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("name1up", studentRepository.findOne(1L).get().getName());
  }

  @Test
  public void addLabProblem() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_ADD, "2, 2, descr2");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(labProblemRepository.findAll().spliterator(), false).count(), 2L);
    labProblemRepository.delete(2L);
  }

  @Test
  public void updateLabProblem() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_UPDATE, "1, 2, descrup");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("descrup", labProblemRepository.findOne(1L).get().getDescription());
  }

  @Test
  public void addAssignment() throws IOException{
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_ADD, "2, 1, 1, 5");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(assignmentsRepository.findAll().spliterator(), false).count(), 2L);
    assignmentsRepository.delete(2L);
  }

  @Test
  public void updateAssignment() throws IOException{
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_UPDATE, "1, 1, 1, 6");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(6, assignmentsRepository.findOne(1L).get().getGrade());
  }

  @Test
  public void deleteAssignment() throws IOException{
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_DELETE, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(assignmentsRepository.findAll().spliterator(), false).count(), 0);
  }

  @Test
  public void deleteLabProblem() throws IOException{
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_DELETE, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(labProblemRepository.findAll().spliterator(), false).count(), 0);
  }

  @Test
  public void deleteStudent() throws IOException{
    Message clientRequest = new Message(MessageHeader.STUDENT_DELETE, "1");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals(StreamSupport.stream(studentRepository.findAll().spliterator(), false).count(), 0);
  }

  @Test
  public void averageGrade() throws IOException{
    Assignment assignment1 = new Assignment(1L, 1L, 2);
    assignment1.setId(2L);
    Assignment assignment2 = new Assignment(1L, 1L, 6);
    assignment2.setId(3L);
    assignmentsRepository.save(assignment1);
    assignmentsRepository.save(assignment2);
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_AVERAGE_GRADE, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("6.0", serverResponse.getBody());
    assignmentsRepository.delete(2L);
    assignmentsRepository.delete(3L);
  }

  @Test
  public void greatestMean() throws IOException{
    Student student2 = new Student("sn2", "name2", 2);
    student2.setId(2L);
    Assignment assignment1 = new Assignment(2L, 1L, 2);
    assignment1.setId(2L);
    studentRepository.save(student2);
    assignmentsRepository.save(assignment1);
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_GREATEST_MEAN, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1, 10.0", serverResponse.getBody());
    assignmentsRepository.delete(2L);
    studentRepository.delete(2L);
  }

  @Test
  public void labProblemMostAssigned() throws IOException{
    Student student2 = new Student("sn2", "name2", 2);
    student2.setId(2L);
    Assignment assignment1 = new Assignment(2L, 1L, 2);
    assignment1.setId(2L);
    studentRepository.save(student2);
    assignmentsRepository.save(assignment1);
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MOST_ASSIGNED, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1, 2", serverResponse.getBody());
    assignmentsRepository.delete(2L);
    studentRepository.delete(2L);
  }

  @Test
  public void studentProblems() throws IOException{
    Student student2 = new Student("sn2", "name2", 2);
    student2.setId(2L);
    LabProblem labProblem2 = new LabProblem(2, "descr2");
    labProblem2.setId(2L);
    Assignment assignment1 = new Assignment(2L, 1L, 2);
    assignment1.setId(2L);
    Assignment assignment2 = new Assignment(2L, 2L, 2);
    assignment1.setId(3L);
    studentRepository.save(student2);
    labProblemRepository.save(labProblem2);
    assignmentsRepository.save(assignment1);
    assignmentsRepository.save(assignment2);
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_PROBLEM_MAPPING, "");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("1,sn1,studentName,1?1,11,description\n2,sn2,name2,2?1,11,description;2,2,descr2", serverResponse.getBody());
    assignmentsRepository.delete(2L);
    assignmentsRepository.delete(3L);
    labProblemRepository.delete(2L);
    studentRepository.delete(2L);
  }
  @Test
  public void assignmentsSorted() throws IOException{ // TODO fix reflection issue due to different folder positioning
    Assignment assignment1 = new Assignment(1L, 1L, 2);
    assignment1.setId(2L);
    assignmentsRepository.save(assignment1);
    Message clientRequest = new Message(MessageHeader.ASSIGNMENT_SORTED, "ASC grade");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("2,1,1,2\n1,1,1,10", serverResponse.getBody());
    assignmentsRepository.delete(2L);
  }

  @Test
  public void studentsSorted() throws IOException{   // TODO fix reflection issue due to different folder positioning
    Student student1 = new Student("sn2", "name", 2);
    student1.setId(2L);
    studentRepository.save(student1);
    Message clientRequest = new Message(MessageHeader.STUDENT_SORTED, "DESC id");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("2,sn2,name,2\n1,sn1,studentName,1", serverResponse.getBody());
    studentRepository.delete(2L);
  }

  @Test
  public void labProblemsSorted() throws IOException{  // TODO fix reflection issue due to different folder positioning
    LabProblem labProblem1 = new LabProblem(2, "descr");
    labProblem1.setId(2L);
    labProblemRepository.save(labProblem1);
    Message clientRequest = new Message(MessageHeader.LABPROBLEM_SORTED, "DESC id");
    clientInputStream = clientSocket.getInputStream();
    clientOutputStream = clientSocket.getOutputStream();
    clientRequest.writeTo(clientOutputStream);
    serverResponse = new Message();
    serverResponse.readFrom(clientInputStream);
    Assert.assertEquals(MessageHeader.OK_REQUEST, serverResponse.getHeader());
    Assert.assertEquals("2,2,descr\n1,1,description", serverResponse.getBody());
    studentRepository.delete(2L);
  }
}