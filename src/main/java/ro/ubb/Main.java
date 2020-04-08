package ro.ubb;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ro.ubb.UI.Console;

public class Main {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("ro.ubb.config");
    Console console = context.getBean(Console.class);
    console.run();
  }
}
