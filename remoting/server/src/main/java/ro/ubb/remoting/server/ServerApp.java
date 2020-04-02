package ro.ubb.remoting.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.remoting.server.repository.containers.StudentRepository;

/**
 * Created by radu.
 */
public class ServerApp {
    public static void main(String[] args) {
        System.out.println("server starting");

        AnnotationConfigApplicationContext context=
                new AnnotationConfigApplicationContext(
                       "ro.ubb.remoting.server.config"
                );

      StudentRepository studentRepository =
          (StudentRepository) context.getBean(
              "studentRepository");


      studentRepository.findAll().forEach(System.out::println);

    }
}
