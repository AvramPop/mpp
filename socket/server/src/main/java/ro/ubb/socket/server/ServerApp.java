package ro.ubb.socket.server;



import ro.ubb.socket.common.infrastructure.MessageHeader;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by radu.
 */
public class ServerApp {

    public static void main(String[] args) {
        System.out.println("server");
        String a = MessageHeader.ASSIGNMENT_ADD;
        System.out.println(a);
    }
}
