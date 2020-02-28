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
import repository.InMemoryRepository;
import repository.Repository;
import service.LabProblemService;
import service.StudentService;

public class Main {
  public static void main(String[] args) {
    Validator<Student> studentValidator = new StudentValidator();
    Validator<LabProblem> labProblemValidator = new LabProblemValidator();
    Repository<Long, Student> studentRepository = new InMemoryRepository<>(studentValidator);
    Repository<Long, LabProblem> labProblemRepository =
        new InMemoryRepository<>(labProblemValidator);
    StudentService studentService = new StudentService(studentRepository);
    LabProblemService labProblemService = new LabProblemService(labProblemRepository);
    Console console = new Console(studentService, labProblemService);
    console.run();
  }
}
