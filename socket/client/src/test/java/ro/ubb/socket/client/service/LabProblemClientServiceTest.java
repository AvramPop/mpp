package ro.ubb.socket.client.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.LabProblem;
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

public class LabProblemClientServiceTest {

  private LabProblemClientService labProblemClientService;
  private ExecutorService executorService;
  private TCPClient tcpClient;
  private LabProblem labProblem;
  private static final Long ID = 1L;
  private static final Long NEW_ID = 2L;
  private static final String DESCRIPTION = "123123";
  private static final String NEW_DESCRIPTION = "123";
  private static final int PROBLEM_NUMBER = 123;
  private static final int NEW_PROBLEM_NUMBER = 999;

  @Before
  public void setUp() {
    executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    tcpClient = new TCPClient();
    labProblemClientService = new LabProblemClientService(executorService, tcpClient);
    labProblem = new LabProblem(PROBLEM_NUMBER, DESCRIPTION);
    labProblem.setId(ID);
  }

  @After
  public void tearDown() {
    labProblemClientService = null;
    executorService.shutdown();
    executorService = null;
    tcpClient = null;
    labProblem = null;
  }

  @Test
  public void getAllLabProblemsFailNoConnection() {

    try {
      labProblemClientService.getAllLabProblems().get();
    } catch (InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void getAllLabProblemsFailResponseBad() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = labProblemClientService.getAllLabProblems();
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
      var result = labProblemClientService.getAllLabProblems();
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1,1");
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
      var result = labProblemClientService.getLabProblemById(1L);
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
      var result = labProblemClientService.getLabProblemById(1L);
      try (Socket serverResponse = serverSocket.accept();
          OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1,2");
        response.writeTo(outputStream);
        LabProblem expectedResponse = new LabProblem(1, "1");
        expectedResponse.setId(1L);
        Assert.assertEquals(result.get(), expectedResponse);
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }
  }

  @Test
  public void addLabProblemsFails() {
    try (ServerSocket serverSocket = new ServerSocket(Message.PORT)) {
      var result = labProblemClientService.addLabProblem(1L, 1, "1");
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
      var result = labProblemClientService.addLabProblem(1L, 1, "1");
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
      var result = labProblemClientService.updateLabProblem(1L, 1, "1");
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
      var result = labProblemClientService.updateLabProblem(1L, 1, "1");
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
      var result = labProblemClientService.deleteLabProblem(1L);
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
      var result = labProblemClientService.deleteLabProblem(1L);
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
      var result = labProblemClientService.getAllLabProblemsSorted(new Sort("asd"));
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
      var result = labProblemClientService.getAllLabProblemsSorted(new Sort("description"));
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
