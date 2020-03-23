package ro.ubb.UI;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ClassReflectionException;
import ro.ubb.domain.exceptions.RepositoryException;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.db.sort.Sort;
import ro.ubb.service.AssignmentService;
import ro.ubb.service.LabProblemService;
import ro.ubb.service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/** Console based user interface */
public class Console {
  private StudentService studentService;
  private LabProblemService labProblemService;
  private AssignmentService assignmentService;
  private HashMap<String, Runnable> dictionaryOfCommands;

  public Console(
      StudentService studentService,
      LabProblemService labProblemService,
      AssignmentService assignmentService) {
    this.studentService = studentService;
    this.labProblemService = labProblemService;
    this.assignmentService = assignmentService;
    // I use lambda methods with a hash table to not to make if statements
    // if the thing fails it gets a null pointer exception
    // which means not a valid command
    initDictionaryOfCommands();
  }

  private void initDictionaryOfCommands() {
    dictionaryOfCommands = new HashMap<>();
    dictionaryOfCommands.put("add student", this::addStudent);
    dictionaryOfCommands.put("print students", this::printStudents);
    dictionaryOfCommands.put("print students sorted", this::printStudentsSorted);
    dictionaryOfCommands.put("print assignments sorted", this::printAssignmentsSorted);
    dictionaryOfCommands.put("print lab problems sorted", this::printLabProblemsSorted);
    dictionaryOfCommands.put("add lab problem", this::addLabProblem);
    dictionaryOfCommands.put("print lab problems", this::printLabProblems);
    dictionaryOfCommands.put("update lab problem", this::updateLabProblem);
    dictionaryOfCommands.put("delete lab problem", this::deleteLabProblem);
    dictionaryOfCommands.put("filter lab problems", this::filterLabProblemsByProblemNumber);
    dictionaryOfCommands.put("update student", this::updateStudent);
    dictionaryOfCommands.put("delete student", this::deleteStudent);
    dictionaryOfCommands.put("filter students", this::filterStudentsByGroup);
    dictionaryOfCommands.put("add assignment", this::addAssignment);
    dictionaryOfCommands.put("print assignments", this::printAssignments);
    dictionaryOfCommands.put("delete assignment", this::deleteAssignment);
    dictionaryOfCommands.put("update assignment", this::updateAssignment);
    dictionaryOfCommands.put("max mean student", this::greatestMeanOfStudent);
    dictionaryOfCommands.put("lab problem most", this::labProblemMostAssigned);
    dictionaryOfCommands.put("avg grade", this::averageGrade);
    dictionaryOfCommands.put("student problems", this::studentProblems);
    dictionaryOfCommands.put("exit", () -> System.exit(0));
  }

  private void printLabProblemsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Sort sort = null;
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        Sort.Direction sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = Sort.Direction.ASC;
        } else if (order.equals("DESC")) {
          sortingDirection = Sort.Direction.DESC;
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (sort == null) {
          sort = new Sort(sortingDirection, columnName);
          sort.setClassName("LabProblem");
        } else {
          sort = sort.and(new Sort(sortingDirection, columnName));
        }
      }
      List<LabProblem> labProblems = labProblemService.getAllLabProblemsSorted(sort);
      labProblems.forEach(System.out::println);
    } catch (IOException e) {
      System.out.println("Invalid input!");
    } catch (ClassReflectionException e) {
      System.err.println(e.getMessage());
    }
  }

  private void printAssignmentsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Sort sort = null;
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        Sort.Direction sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = Sort.Direction.ASC;
        } else if (order.equals("DESC")) {
          sortingDirection = Sort.Direction.DESC;
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (sort == null) {
          sort = new Sort(sortingDirection, columnName);
          sort.setClassName("Assignment");
        } else {
          sort = sort.and(new Sort(sortingDirection, columnName));
        }
      }
      List<Assignment> assignments = assignmentService.getAllAssignmentsSorted(sort);
      assignments.forEach(System.out::println);
    } catch (IOException e) {
      System.out.println("Invalid input!");
    } catch (ClassReflectionException e) {
      System.err.println(e.getMessage());
    }
  }

  private void printStudentsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Sort sort = null;
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        Sort.Direction sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = Sort.Direction.ASC;
        } else if (order.equals("DESC")) {
          sortingDirection = Sort.Direction.DESC;
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (sort == null) {
          sort = new Sort(sortingDirection, columnName);
          sort.setClassName("Student");
        } else {
          sort = sort.and(new Sort(sortingDirection, columnName));
        }
      }
      List<Student> students = studentService.getAllStudentsSorted(sort);
      students.forEach(System.out::println);
    } catch (IOException e) {
      System.out.println("Invalid input!");
    } catch (ClassReflectionException e) {
      System.err.println(e.getMessage());
    }
  }

  private void studentProblems() {
    Optional<Map<Student, List<LabProblem>>> studentsLabProblems =
        assignmentService.studentAssignedProblems();
    Student emptyStudent = new Student();
    if (studentsLabProblems.isPresent()) {
      for (Map.Entry<Student, List<LabProblem>> entry : studentsLabProblems.get().entrySet()) {
        if (!entry.getKey().getSerialNumber().equals("")) {
          System.out.println(entry.getKey().toString());
          System.out.println("Problems:");
          entry
              .getValue()
              .forEach(
                  labProblem -> {
                    System.out.println(labProblem.toString());
                  });
        }
      }
      System.out.println("Unused problems:");
      studentsLabProblems.get().get(emptyStudent).forEach(System.out::println);
    }
  }
  /*
  private void greatestMeanOfGroup() {
    Optional<AbstractMap.SimpleEntry<Integer, Double>> greatestMean = assignmentService.groupWithGreatestMean();
    if (greatestMean.isPresent()) {
      System.out.println(
          "The greatest mean is of group id = "
              + greatestMean.get().getKey()
              + ": "
              + greatestMean.get().getValue());
    } else {
      System.err.println("no students or assignments");
    }
  }
  */
  private void averageGrade() {
    Optional<Double> mean = assignmentService.averageGrade();
    if (mean.isPresent()) {
      System.out.println("The mean of all assignments is " + mean.get());
    } else {
      System.err.println("assignments");
    }
  }

  private void labProblemMostAssigned() {
    Optional<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned =
        assignmentService.idOfLabProblemMostAssigned();
    if (idOfLabProblemMostAssigned.isPresent()) {
      System.out.println(
          "lab problem most assigned id: "
              + idOfLabProblemMostAssigned.get().getKey()
              + " - "
              + idOfLabProblemMostAssigned.get().getValue()
              + "times");
    } else {
      System.err.println("no lab problems assigned");
    }
  }

  private void greatestMeanOfStudent() {
    Optional<AbstractMap.SimpleEntry<Long, Double>> greatestMean = assignmentService.greatestMean();
    if (greatestMean.isPresent()) {
      System.out.println(
          "The greatest mean is of student id = "
              + greatestMean.get().getKey()
              + ": "
              + greatestMean.get().getValue());
    } else {
      System.err.println("no students or assignments");
    }
  }

  /** ro.ubb.UI method for printing the console menu */
  private void printMenu() {
    System.out.println("Menu options:");
    System.out.println("- add student");
    System.out.println("- print students");
    System.out.println("- add lab problem");
    System.out.println("- print lab problems");
    System.out.println("- update lab problem");
    System.out.println("- delete lab problem");
    System.out.println("- filter lab problems [by number]");
    System.out.println("- update student");
    System.out.println("- delete student");
    System.out.println("- filter students [by group]");
    System.out.println("- add assignment");
    System.out.println("- print assignments");
    System.out.println("- update assignment");
    System.out.println("- delete assignment");
    System.out.println("- max mean student");
    System.out.println("- lab problem most");
    System.out.println("- avg grade");
    System.out.println("- student problems");
    System.out.println("- print students sorted");
    System.out.println("- print assignments sorted");
    System.out.println("- print lab problems sorted");
    System.out.println("- exit");
  }

  /** ro.ubb.Main loop of the user interface */
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

  private void printAssignments() {

    Set<Assignment> students = assignmentService.getAllAssignments();
    students.forEach(System.out::println);
  }

  private void addAssignment() {
    System.out.println("Read assignment {id, studentId, labProblemId}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter studentId: ");
      long studentId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter labProblemId: ");
      long labProblemId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter grade: ");
      int grade = Integer.parseInt(input.readLine().strip());
      if (assignmentService.addAssignment(id, studentId, labProblemId, grade).isEmpty())
        System.out.println("Assignment added");
      else System.out.println("Assignment not added");
    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    } catch (RepositoryException ex) {
      System.out.println("Invalid assignment, wrong student or lab problem ID");
    }
  }

  /** ro.ubb.UI method for adding a student */
  private void addStudent() {
    System.out.println("Read student {id,serialNumber, name, group}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter serial number: ");
      String serialNumber = input.readLine().strip();
      System.out.println("Enter name: ");
      String name = input.readLine().strip();
      System.out.println("Enter group: ");
      int group = Integer.parseInt(input.readLine().strip());
      if (studentService.addStudent(id, serialNumber, name, group).isEmpty())
        System.out.println("Student added");
      else System.out.println("Student not added");

    } catch (ValidatorException ex) {
      System.out.println(ex.getMessage());
    } catch (IOException | NumberFormatException e) {
      //      e.printStackTrace();
      System.out.println("invalid input");
    }
  }
  /** ro.ubb.UI method for printing all students */
  private void printStudents() {
    Set<Student> students = studentService.getAllStudents();
    students.forEach(System.out::println);
  }
  /** ro.ubb.UI method for adding a lab problem */
  private void addLabProblem() {
    System.out.println("Read lab problem {id, problem number, description}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter problem number: ");
      int problemNumber = Integer.parseInt(input.readLine().strip());
      System.out.println("Enter description: ");
      String description = input.readLine().strip();
      if (labProblemService.addLabProblem(id, problemNumber, description).isEmpty())
        System.out.println("Lab Problem added");
      else System.out.println("Lab Problem not added");
    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  /** ro.ubb.UI method for printing all lab problems */
  private void printLabProblems() {
    Set<LabProblem> students = labProblemService.getAllLabProblems();
    students.forEach(System.out::println);
  }
  /** ro.ubb.UI method update a lab problem */
  private void updateLabProblem() {
    System.out.println("Read lab problem {id, problem number, description}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter problem number: ");
      int problemNumber = Integer.parseInt(input.readLine().strip());
      System.out.println("Enter description: ");
      String description = input.readLine().strip();
      if (labProblemService.updateLabProblem(id, problemNumber, description).isEmpty())
        System.out.println("Lab Problem updated");
      else System.out.println("Lab Problem not updated");
    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  /** ro.ubb.UI method deletes a lab problem */
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
    if (assignmentService.deleteLabProblem(id).isEmpty()) System.out.println("Delete failed");
    else System.out.println("Delete successful");
  }
  /** ro.ubb.UI method filters lab problems by problem number */
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

    Set<LabProblem> labProblems = labProblemService.filterByProblemNumber(problemNumber);
    labProblems.forEach(System.out::println);
  }

  /** ro.ubb.UI method update a student */
  private void updateStudent() {
    System.out.println("Update student {id,serialNumber, name, group}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter serial number: ");
      String serialNumber = input.readLine().strip();
      System.out.println("Enter name: ");
      String name = input.readLine().strip();
      System.out.println("Enter group: ");
      int group = Integer.parseInt(input.readLine().strip());
      if (studentService.updateStudent(id, serialNumber, name, group).isEmpty())
        System.out.println("Student updated");
      else System.out.println("Student not updated");
    } catch (ValidatorException ex) {
      // ex.printStackTrace();
      System.out.println(ex.getMessage());
    } catch (IOException | NumberFormatException e) {
      //      e.printStackTrace();
      System.err.println("invalid input");
    }
  }
  /** ro.ubb.UI method deletes a student */
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
    if (assignmentService.deleteStudent(id).isEmpty()) System.out.println("Delete failed");
    else System.out.println("Delete successful");
  }
  /** ro.ubb.UI method filters students by group number */
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

    Set<Student> students = studentService.filterByGroup(groupNumber);
    students.forEach(System.out::println);
  }

  private void updateAssignment() {
    System.out.println("Update assignment {id, studentId, labProblemId}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter studentId: ");
      long studentId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter labProblemId: ");
      long labProblemId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter grade: ");
      int grade = Integer.parseInt(input.readLine().strip());
      if (assignmentService.updateAssignment(id, studentId, labProblemId, grade).isEmpty())
        System.out.println("Assignment updated");
      else System.out.println("Assignment not updated");
    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    } catch (RepositoryException ex) {
      System.out.println("Invalid assignment, wrong student or lab problem ID");
    }
  }

  private void deleteAssignment() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());
      if (assignmentService.deleteAssignment(id).isEmpty()) System.out.println("Delete failed");
      else System.out.println("Delete successful");

    } catch (IOException | NumberFormatException e) {
      System.err.println("bad input");
    }
  }
}
