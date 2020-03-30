package ro.ubb.socket.client.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.service.sort.Sort;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentClientServiceTest {

  private StudentClientService studentClientService;
  private ExecutorService executorService;
  private TCPClient tcpClient;
  private Student student;

  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String SERIAL_NUMBER = "sn01";
  private static final String NEW_SERIAL_NUMBER = "sn02";
  private static final String NAME = "studentName";
  private static final String NEW_NAME = "newStudentName";
  private static final int GROUP = 123;
  private static final int NEW_GROUP = 999;

  @Before
  public void setUp() {
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    tcpClient = new TCPClient();
    studentClientService = new StudentClientService(executorService, tcpClient);
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);
  }

  @After
  public void tearDown() {
    studentClientService = null;
    executorService.shutdown();
    executorService = null;
    tcpClient = null;
    student = null;
  }

  @Test
  public void getAllStudentsFailNoConnection() {

    try {
      studentClientService.getAllStudents().get();
    } catch (InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getAllStudentFailResponseBad() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getAllStudents();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getAllStudentSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getAllStudents();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, student.objectToFileLine());
        response.writeTo(outputStream);
        Assert.assertEquals(result.get().size(), 1);
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void getStudentByIdFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getStudentById(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getStudentByIdSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getStudentById(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, student.objectToFileLine());
        response.writeTo(outputStream);
        Assert.assertEquals(result.get(), student);
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void addStudentFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.addStudent(1L, "1", "1", 1);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void addStudentSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.addStudent(1L, "1", "1", 1);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void updateStudentFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.updateStudent(1L, "1", "1", 1);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void updateStudentSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.updateStudent(1L, "1", "1", 1);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void deleteStudentFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.deleteStudent(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void deleteStudentSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.deleteStudent(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void getAllStudentsSortedFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getAllStudentsSorted(new Sort("asd"));
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.BAD_REQUEST, "");
        response.writeTo(outputStream);
        result.get();
        Assert.fail();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getAllStudentsSortedSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = studentClientService.getAllStudentsSorted(new Sort("name"));
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1,a,1\n2,1,b,1");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }
}
