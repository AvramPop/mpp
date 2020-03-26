package ro.ubb.socket.client;


import ro.ubb.socket.common.domain.exceptions.ServiceException;
import ro.ubb.socket.common.infrastructure.Message;
import ro.ubb.socket.common.infrastructure.MessageHeader;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by radu.
 */
public class ClientApp {
    public static void main(String[] args) {
        Message request = new Message(MessageHeader.STUDENT_BY_ID, "1");
        try (var socket = new Socket(Message.HOST, Message.PORT);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()
        ) {
            System.out.println("sendAndReceive - sending request: " + request);
            request.writeTo(os);

            System.out.println("sendAndReceive - received response: ");
            Message response = new Message();
            response.readFrom(is);
            System.out.println(response);
        } catch (IOException e) {
            throw new ServiceException("error connection to server " + e.getMessage(), e);
        }
    }
}
