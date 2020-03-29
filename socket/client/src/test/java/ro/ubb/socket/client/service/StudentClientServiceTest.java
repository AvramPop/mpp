package ro.ubb.socket.client.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;

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
    studentClientService = new StudentClientService(executorService,tcpClient);
    student = new Student(SERIAL_NUMBER, NAME, GROUP);
    student.setId(ID);

  }

  @After
  public void tearDown()  {
    executorService.shutdown();
    executorService = null;


  }

  @Test
  public void getAllStudentsFailNoConnection(){

    try{
      studentClientService.getAllStudents().get();
    } catch (InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }

  }

  @Test
  public void getAllStudentFailResponseBad()  {
    try(Socket serverSocket = new Socket(Message.HOST, Message.TEST_PORT);
        OutputStream serverOutputStream = serverSocket.getOutputStream()) {

      var result = studentClientService.getAllStudents();
      Message response = new Message(MessageHeader.BAD_REQUEST,"");
      response.writeTo(serverOutputStream);
      result.get();
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.assertTrue(true);
    }

  }
  @Test
  public void getAllStudentSuccess()  {
    try(ServerSocket serverSocket = new ServerSocket( Message.PORT)) {
      var result = studentClientService.getAllStudents();
      try (Socket serverResponse = serverSocket.accept(); OutputStream outputStream = serverResponse.getOutputStream()) {

        Message response = new Message(MessageHeader.OK_REQUEST, "1,1,1,1");
        response.writeTo(outputStream);
        result.get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      Assert.fail();
    }

  }

}