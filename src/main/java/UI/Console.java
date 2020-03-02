package UI;

import domain.LabProblem;
import domain.Student;
import domain.exceptions.ValidatorException;
import service.LabProblemService;
import service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;

/** Console based user interface */
public class Console {
  private StudentService studentController;
  private LabProblemService lapProblemController;
  private HashMap<String, Runnable> dictionaryOfCommands;

  public Console(StudentService studentController, LabProblemService lapProblemController) {
    this.studentController = studentController;
    this.lapProblemController = lapProblemController;
    dictionaryOfCommands = new HashMap<>();
    dictionaryOfCommands.put(
        "add student",
        this::addStudent); // I use lambda methods with a hash table to not to make if statements
    dictionaryOfCommands.put(
        "print students",
        this::printStudents); // if the thing fails it gets a null pointer exception
    dictionaryOfCommands.put(
        "add lab problem", this::addLabProblem); // which means not a valid command
    dictionaryOfCommands.put("print lab problems", this::printLabProblems);
    dictionaryOfCommands.put("exit", () -> System.exit(0));
  }

  /** Main loop of the user interface */
  public void run() {
    while (true) {
      printMenu();
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      try {
        String inputString = input.readLine();
        dictionaryOfCommands.get(inputString).run();
      } catch (ValidatorException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      } catch (IOException ex) {
        System.out.println("Error with input!");
      } catch (NullPointerException ex) {
        System.out.println("Not a vaild comand");
      }
    }
  }

  /**
   * UI method for printing the console menu
   */
  private void printMenu(){
    System.out.println("Menu options:");
    System.out.println("1. Add student");
    System.out.println("2. Print students");
    System.out.println("3. Add lab problem");
    System.out.println("4. Print lab problems");
    System.out.println("5. exit");
  }

  /** UI method for adding a student */
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
  /** UI method for adding a lab problem */
  private void addLabProblem() {
    LabProblem newLabProblem = readLabProblem();
    if (newLabProblem == null) {
      System.out.println("Invalid input! ID must be positive number");
      return;
    }
    lapProblemController.addLabProblem(newLabProblem);
    System.out.println("Lab Problem added");
  }
  /** UI method for printing all students */
  private void printStudents() {
    Set<Student> students = studentController.getAllStudents();
    students.forEach(System.out::println);
  }
  /** UI method for printing all lab problems */
  private void printLabProblems() {
    Set<LabProblem> students = lapProblemController.getAllLabProblems();
    students.forEach(System.out::println);
  }
  /** UI method for reading a new lab problem from the user */
  private LabProblem readLabProblem() {
    System.out.println("Read lab problem {id, problem number, description}");
    LabProblem newLabProblem = null;
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter problem number: ");
      int problemNumber = Integer.parseInt(input.readLine().strip());
      System.out.println("Enter description: ");
      String description = input.readLine().strip();
      newLabProblem = new LabProblem(problemNumber, description);
      newLabProblem.setId(id);
    } catch (IOException | NumberFormatException ex) {
      // ex.printStackTrace();
    }
    return newLabProblem;
  }
  /** UI method for reading a new student from the user */
  private Student readStudent() {
    System.out.println("Read student {id,serialNumber, name, group}");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Student newStudent = null;
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter serial number: ");
      String serialNumber = input.readLine().strip();
      System.out.println("Enter name: ");
      String name = input.readLine().strip();
      System.out.println("Enter group: ");
      int group = Integer.parseInt(input.readLine().strip());
      newStudent = new Student(serialNumber, name, group);
      newStudent.setId(id);
    } catch (IOException | NumberFormatException ex) {
      // ex.printStackTrace();
    }
    return newStudent;
  }
}
