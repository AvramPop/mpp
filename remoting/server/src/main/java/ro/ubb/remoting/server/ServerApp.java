package ro.ubb.remoting.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.remoting.common.service.sort.Sort;
import ro.ubb.remoting.server.repository.AssignmentRepository;

/** Created by radu. */
public class ServerApp {
  public static void main(String[] args) {
    System.out.println("server starting");

    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("ro.ubb.remoting.server.config");
  }
}
