package UI;

import domain.LabProblem;
import domain.Student;
import domain.exceptions.RepositoryException;
import domain.exceptions.ValidatorException;
import service.LabProblemService;
import service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

public class Console {
  private StudentService studentController;
  private LabProblemService lapProblemController;
  private HashMap<String, Runnable> dictionaryOfCommands;

  public Console(StudentService studentController, LabProblemService lapProblemController) {
    this.studentController = studentController;
    this.lapProblemController = lapProblemController;
    dictionaryOfCommands = new HashMap<>();
    dictionaryOfCommands.put("add student", this::addStudent);
    dictionaryOfCommands.put("print students", this::printStudents);
    dictionaryOfCommands.put("add lab problem", this::addLabProblem);
    dictionaryOfCommands.put("print lab problems", this::printLabProblems);
    dictionaryOfCommands.put("exit", () -> System.exit(0));
  }

  public void run() {
    while (true) {
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      try {
        String inputString = input.readLine();
        dictionaryOfCommands.get(inputString).run();
      } catch (RepositoryException | ValidatorException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      } catch (IOException ex) {
        System.out.println("Error with input!");
      } catch (Exception e) {
        System.out.println("Not existing command!");
      }
    }
  }

  private void addStudent() {
    try {
      Student newStudent = readStudent();
      if (newStudent == null) {
        System.out.println("Invalid input! ID must be positive number");
        return;
      }
      studentController.addStudent(newStudent);
      System.out.println("Student added!");
    } catch (ValidatorException ex) {
      // ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
  }

  private void addLabProblem() {
    try {
      LabProblem newLabProblem = readLabProblem();
      if (newLabProblem == null) {
        System.out.println("Invalid input! ID must be positive number");
        return;
      }
      lapProblemController.addLabProblem(newLabProblem);
      System.out.println("Lab Problem added");
    } catch (ValidatorException ex) {
      // ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
  }

  private void printStudents() {
    Set<Student> students = studentController.getAllStudents();
    students.forEach(System.out::println);
  }

  private void printLabProblems() {
    Set<LabProblem> students = lapProblemController.getAllLabProblems();
    students.forEach(System.out::println);
  }

  private LabProblem readLabProblem() {
    System.out.println("Read lab problem {id, problem number, description}");
    LabProblem newLabProblem = null;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      long id = Long.parseLong(input.readLine().strip());
      int problemNumber = Integer.parseInt(input.readLine().strip());
      String description = input.readLine().strip();
      newLabProblem = new LabProblem(problemNumber, description);
      newLabProblem.setId(id);
    } catch (IOException | NumberFormatException ex) {
      // ex.printStackTrace();
    }
    return newLabProblem;
  }

  private Student readStudent() {
    System.out.println("Read student {id,serialNumber, name, group}");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Student newStudent = null;
    try {
      long id = Long.parseLong(input.readLine().strip());
      String serialNumber = input.readLine().strip();
      String name = input.readLine().strip();
      int group = Integer.parseInt(input.readLine().strip());
      newStudent = new Student(serialNumber, name, group);
      newStudent.setId(id);
    } catch (IOException | NumberFormatException ex) {
      // ex.printStackTrace();
    }
    return newStudent;
  }
}
