package ro.ubb.socket.client;

import ro.ubb.socket.client.infrastructure.TCPClient;
import ro.ubb.socket.client.service.AssignmentClientService;
import ro.ubb.socket.client.service.LabProblemClientService;
import ro.ubb.socket.client.service.StudentClientService;
import ro.ubb.socket.client.ui.Console;
import ro.ubb.socket.common.domain.exceptions.ServiceException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Created by radu. */
public class ClientApp {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    TCPClient tcpClient = new TCPClient();
    StudentClientService studentClientService = new StudentClientService(executorService,tcpClient);
    LabProblemClientService labProblemClientService = new LabProblemClientService(executorService,tcpClient);
    AssignmentClientService assignmentClientService = new AssignmentClientService(executorService,tcpClient);
    Console console = new Console(studentClientService,labProblemClientService,assignmentClientService,executorService);
    console.run();
    executorService.shutdown();

  }
}
