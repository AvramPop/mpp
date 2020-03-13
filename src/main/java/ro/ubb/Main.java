package ro.ubb;
// P2. Lab problems
//    A teacher manages information about students and lab problems.
//    Create an application which allows to:
//    perform CRUD operations on students and lab problems
//    assign problems to students; assign grades
//    filter entities based on various criteria
//    reports: e.g. find the problem that was assigned most times

import ro.ubb.UI.Console;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.validators.AssignmentValidator;
import ro.ubb.domain.validators.LabProblemValidator;
import ro.ubb.domain.validators.StudentValidator;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.*;
import ro.ubb.service.AssignmentService;
import ro.ubb.service.LabProblemService;
import ro.ubb.service.StudentService;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) {
    try {
      Validator<Student> studentValidator = new StudentValidator();
      Validator<LabProblem> labProblemValidator = new LabProblemValidator();
      Validator<Assignment> assignmentValidator = new AssignmentValidator();
      // Repository<Long, Student> studentRepository = new InMemoryRepository<>(studentValidator);
      // Repository<Long, LabProblem> labProblemRepository =
      // new InMemoryRepository<>(labProblemValidator);
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathTextFile("labProblems")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathTextFile("assignments")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathTextFile("students")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathXMLFile("labProblems")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathXMLFile("assignments")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPathXMLFile("students")));
      } catch (FileAlreadyExistsException ignored) {
      }

      /*
      Repository<Long, Student> studentRepository =
          new FileRepository<>(
              repoPathTextFile("students"),
              ";",
              FileLineEntityFactory.studentFromFileLine());

      Repository<Long, LabProblem> labProblemRepository =
          new FileRepository<>(
              repoPathTextFile("labProblems"),
              ";",
                  FileLineEntityFactory.labProblemFromFileLine());

      Repository<Long, Assignment> assignmentRepository =
              new FileRepository<>(
                      repoPathTextFile("assignments"),
                      ";",
                      FileLineEntityFactory.assignmentObjectFromFileLine());
*/

      Repository<Long, Student> studentRepository =
          new XMLRepository<>(
              repoPathXMLFile("students"), XMLElementToEntityFacotry.studentObjectFromXMLFile());
      Repository<Long, LabProblem> labProblemRepository =
          new XMLRepository<>(
              repoPathXMLFile("labProblems"),
              XMLElementToEntityFacotry.labProblemObjectFromXMLFile());
      Repository<Long, Assignment> assignmentRepository =
          new XMLRepository<>(
              repoPathXMLFile("assignments"),
              XMLElementToEntityFacotry.assignmentObjectFromXMLFile());

      StudentService studentService = new StudentService(studentRepository, studentValidator);
      LabProblemService labProblemService =
          new LabProblemService(labProblemRepository, labProblemValidator);
      AssignmentService assignmentService =
          new AssignmentService(
              assignmentRepository, labProblemService, studentService, assignmentValidator);
      Console console = new Console(studentService, labProblemService, assignmentService);
      console.run();
    } catch (IOException ex) {
      System.out.println("Can't create files\nTerminating...");
    }
  }

  public static String repoPathTextFile(String repoName) {
    return "src"
        + FileSystems.getDefault().getSeparator()
        + "main"
        + FileSystems.getDefault().getSeparator()
        + "resources"
        + FileSystems.getDefault().getSeparator()
        + repoName
        + ".txt";
  }

  public static String repoPathXMLFile(String repoName) {
    return "src"
        + FileSystems.getDefault().getSeparator()
        + "main"
        + FileSystems.getDefault().getSeparator()
        + "resources"
        + FileSystems.getDefault().getSeparator()
        + repoName
        + ".xml";
  }
}
