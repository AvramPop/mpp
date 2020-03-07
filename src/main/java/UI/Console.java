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
  private LabProblemService labProblemController;
  private HashMap<String, Runnable> dictionaryOfCommands;

  public Console(StudentService studentController, LabProblemService labProblemController) {
    this.studentController = studentController;
    this.labProblemController = labProblemController;
    // I use lambda methods with a hash table to not to make if statements
    // if the thing fails it gets a null pointer exception
    // which means not a valid command
    initDictionaryOfCommands();
  }

  private void initDictionaryOfCommands(){
    dictionaryOfCommands = new HashMap<>();
    dictionaryOfCommands.put(
        "add student",
        this::addStudent);
    dictionaryOfCommands.put(
        "print students",
        this::printStudents);
    dictionaryOfCommands.put(
        "add lab problem", this::addLabProblem);
    dictionaryOfCommands.put("print lab problems", this::printLabProblems);
    dictionaryOfCommands.put("update lab problem", this::updateLabProblem);
    dictionaryOfCommands.put("delete lab problem", this::deleteLabProblem);
    dictionaryOfCommands.put("filter lab problems", this::filterLabProblemsByProblemNumber);
    dictionaryOfCommands.put("update student", this::updateStudent);
    dictionaryOfCommands.put("delete student", this::deleteStudent);
    dictionaryOfCommands.put("filter students", this::filterStudentsByGroup);
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

  /** UI method for printing the console menu */
  private void printMenu() {
    System.out.println("Menu options:");
    System.out.println("- add student");
    System.out.println("- print students");
    System.out.println("- add lab problem");
    System.out.println("- print lab problems");
    System.out.println("- update lab problem");
    System.out.println("- delete lab problem");
    System.out.println("- filter lab problems");
    System.out.println("- update student");
    System.out.println("- delete student");
    System.out.println("- filter students");
    System.out.println("- exit");
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

    } catch (ValidatorException ex) {
      // ex.printStackTrace();
      System.out.println(ex.getMessage());
    }
  }
  /** UI method for printing all students */
  private void printStudents() {
    Set<Student> students = studentController.getAllStudents();
    students.forEach(System.out::println);
  }
  /** UI method for adding a lab problem */
  private void addLabProblem() {
    LabProblem newLabProblem = readLabProblem();
    if (newLabProblem == null) {
      System.out.println("Invalid input! ID must be positive number");
      return;
    }
    if (labProblemController.addLabProblem(newLabProblem).isEmpty())
      System.out.println("Lab Problem added");
    else System.out.println("Lab Problem not added");
  }
  /** UI method for printing all lab problems */
  private void printLabProblems() {
    Set<LabProblem> students = labProblemController.getAllLabProblems();
    students.forEach(System.out::println);
  }
  /** UI method update a lab problem */
  private void updateLabProblem() {
    LabProblem newLabProblem = readLabProblem();
    if (newLabProblem == null) {
      System.out.println("Invalid input! ID must be positive number");
      return;
    }
    if (labProblemController.updateLabProblem(newLabProblem).isEmpty())
      System.out.println("Lab Problem updated");
    else System.out.println("Lab Problem not updated");
  }
  /** UI method deletes a lab problem */
  private void deleteLabProblem() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
      return;
    }
    labProblemController.deleteLabProblem(id);
  }
  /** UI method filters lab problems by problem number */
  private void filterLabProblemsByProblemNumber() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    int problemNumber;
    try {
      System.out.println("Enter problem number: ");
      problemNumber = Integer.parseInt(input.readLine().strip());

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
      return;
    }

    Set<LabProblem> labProblems = labProblemController.filterByProblemNumber(problemNumber);
    labProblems.forEach(System.out::println);
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
      System.out.println("Invalid input!");
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
      System.out.println("Invalid input!");
    }
    return newStudent;
  }

  /** UI method update a student */
  private void updateStudent() {
    Student newStudent = readStudent();
    if (newStudent == null) {
      System.out.println("Invalid input! ID must be positive number");
      return;
    }
    if (studentController.updateStudent(newStudent).isEmpty())
      System.out.println("Student updated");
    else System.out.println("Student not updated");
  }
  /** UI method deletes a student */
  private void deleteStudent() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
      return;
    }
    studentController.deleteStudent(id);
  }
  /** UI method filters students by group number */
  private void filterStudentsByGroup() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    int groupNumber;
    try {
      System.out.println("Enter group number: ");
      groupNumber = Integer.parseInt(input.readLine().strip());

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
      return;
    }

    Set<Student> students = studentController.filterByGroup(groupNumber);
    students.forEach(System.out::println);
  }
}
