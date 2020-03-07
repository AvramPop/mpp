// P2. Lab problems
//    A teacher manages information about students and lab problems.
//    Create an application which allows to:
//    perform CRUD operations on students and lab problems
//    assign problems to students; assign grades
//    filter entities based on various criteria
//    reports: e.g. find the problem that was assigned most times

import UI.Console;
import domain.LabProblem;
import domain.Student;
import domain.validators.LabProblemValidator;
import domain.validators.StudentValidator;
import domain.validators.Validator;
import repository.FileRepository;
import repository.Repository;
import service.LabProblemService;
import service.StudentService;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    try {
      Validator<Student> studentValidator = new StudentValidator();
      Validator<LabProblem> labProblemValidator = new LabProblemValidator();
      //Repository<Long, Student> studentRepository = new InMemoryRepository<>(studentValidator);
      // Repository<Long, LabProblem> labProblemRepository =
      // new InMemoryRepository<>(labProblemValidator);
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPath("labProblems")));
      } catch (FileAlreadyExistsException ignored) {
      }
      try { // TODO probably should use try-with-resources
        Files.createFile(Paths.get(repoPath("students")));
      } catch (FileAlreadyExistsException ignored) {
      }
      Repository<Long, Student> studentRepository =
          new FileRepository<>(
              studentValidator,
              repoPath("students"),
              ";",
              (line, delimiter) -> {
                List<String> params = Arrays.asList(line.split(delimiter));
                Student student =
                    new Student(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
                student.setId(Long.parseLong(params.get(0)));
                return student;
              });

      Repository<Long, LabProblem> labProblemRepository =
          new FileRepository<>(
              labProblemValidator,
              repoPath("labProblems"),
              ";",
              (line, delimiter) -> {
                List<String> params = Arrays.asList(line.split(delimiter));
                LabProblem labProblem =
                    new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
                labProblem.setId(Long.parseLong(params.get(0)));
                return labProblem;
              });
      StudentService studentService = new StudentService(studentRepository);
      LabProblemService labProblemService = new LabProblemService(labProblemRepository);
      Console console = new Console(studentService, labProblemService);
      console.run();
    } catch (IOException ex) {
      System.out.println("Can't create files\nTerminating...");
    }
  }

  public static String repoPath(String repoName) {
    return "src"
        + FileSystems.getDefault().getSeparator()
        + "main"
        + FileSystems.getDefault().getSeparator()
        + "resources"
        + FileSystems.getDefault().getSeparator()
        + repoName
        + ".txt";
  }
}
