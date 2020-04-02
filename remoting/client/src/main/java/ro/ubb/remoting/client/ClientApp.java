package ro.ubb.remoting.client;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.remoting.client.ui.Console;
import ro.ubb.remoting.common.service.AssignmentService;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.StudentService;

/** Created by radu. */
public class ClientApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("ro.ubb.remoting.client.config");

    StudentService studentService = context.getBean(StudentService.class);
    LabProblemService labProblemService = context.getBean(LabProblemService.class);
    AssignmentService assignmentService = context.getBean(AssignmentService.class);

    Console console = new Console(studentService, labProblemService, assignmentService);
    console.run();

    System.out.println("bye");
  }
}
