package ro.ubb.socket.client.infrastructure;

import ro.ubb.socket.common.domain.exceptions.ServiceException;
import ro.ubb.socket.common.infrastructure.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {

  public Message sendAndReceive(Message request) {
    try (Socket socket = new Socket(Message.HOST, Message.PORT);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream()) {
      System.out.println("sendAndReceive - sending request: " + request);
      request.writeTo(os);

      System.out.println("sendAndReceive - received response: ");
      Message response = new Message();
      response.readFrom(is);
      System.out.println(response);

      return response;
    } catch (IOException e) {
      throw new ServiceException("error connection to server " + e.getMessage(), e);
    }
  }
}
