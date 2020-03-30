package ro.ubb.socket.client.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;
import ro.ubb.socket.common.service.sort.Sort;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AssignmentClientServiceTest {
  private static final Long IDASSIGNMENT = 1L;
  private static final Long IDLABPROBLEM = 1L;
  private static final Long IDSTUDENT = 1L;
  private static final Long NEW_IDASSIGNMENT = 2L;
  private static final int GRADE = 10;
  private Assignment assignment;
  private ExecutorService executorService;
  private TCPClient tcpClient;
  private AssignmentClientService assignmentClientService;

  @Before
  public void setUp() {
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    tcpClient = new TCPClient();
    assignmentClientService = new AssignmentClientService(executorService, tcpClient);
    assignment = new Assignment(IDSTUDENT, IDLABPROBLEM, GRADE);
    assignment.setId(IDASSIGNMENT);
  }

  @After
  public void tearDown() {
    assignmentClientService = null;
    executorService.shutdown();
    executorService = null;
    tcpClient = null;
    assignment = null;
  }

  @Test
  public void getAllLabProblemsFailNoConnection() {

    try {
      assignmentClientService.getAllAssignments().get();
    } catch (InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getAllLabProblemsFailResponseBad() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAllAssignments();
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
  public void getAllLabProblemsSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAllAssignments();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, assignment.objectToFileLine());
        response.writeTo(outputStream);
        Assert.assertEquals(result.get().size(), 1);
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void getLabProblemsByIdFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAssignmentById(1L);
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
  public void getLabProblemsByIdSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAssignmentById(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, assignment.objectToFileLine());
        response.writeTo(outputStream);
        Assert.assertEquals(result.get(), assignment);
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void addLabProblemsFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.addAssignment(1L, 1L, 1L, 10);
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
  public void addLabProblemsSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.addAssignment(1L, 1L, 1L, 10);
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
  public void updateLabProblemsFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.addAssignment(1L, 1L, 1L, 10);
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
  public void updateLabProblemsSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.updateAssignment(1L, 1L, 1L, 10);
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
  public void deleteLabProblemsFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.deleteAssignment(1L);
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
  public void deleteLabProblemsSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.deleteAssignment(1L);
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
  public void getAllLabProblemsSortedFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAllAssignmentsSorted(new Sort("asd"));
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
  public void getAllLabProblemsSortedSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.getAllAssignmentsSorted(new Sort("grade"));
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1,1,5\n2,1,2,10");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void greatestMeanFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.greatestMean();
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
  public void greatestMeanSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.greatestMean();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {
        Message response = new Message(MessageHeader.OK_REQUEST, "20,1.8");
        response.writeTo(outputStream);
        Assert.assertEquals(result.get(), new AbstractMap.SimpleEntry<>(20L, 1.8));
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void idOfLabProblemMostAssignedFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.idOfLabProblemMostAssigned();
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
  public void idOfLabProblemMostAssignedSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.idOfLabProblemMostAssigned();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1");
        response.writeTo(outputStream);
        result.get();
        Assert.assertEquals(result.get(), new AbstractMap.SimpleEntry<>(1L, 1L));
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void averageGradeFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.averageGrade();
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
  public void averageGradeSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.averageGrade();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1.32323");
        response.writeTo(outputStream);
        result.get();
        Assert.assertEquals(result.get(), new Double(1.32323));
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void studentAssignedProblemsFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.studentAssignedProblems();
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
  public void studentAssignedProblemsSuccess() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = assignmentClientService.studentAssignedProblems();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response =
            new Message(
                MessageHeader.OK_REQUEST,
                "1,1,asd,1?12,12,lab problem 12 description;5,5,lab problem 5 description;6,6,lab problem 6 description\n");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }
}
