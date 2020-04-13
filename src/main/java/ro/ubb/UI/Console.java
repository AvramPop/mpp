package ro.ubb.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.Student;
import ro.ubb.domain.exceptions.ClassReflectionException;
import ro.ubb.domain.exceptions.RepositoryException;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.repository.sort.Sort;
import ro.ubb.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Component
public class Console {
  @Autowired private StudentService studentService;
  @Autowired private LabProblemService labProblemService;
  @Autowired private AssignmentService assignmentService;
  private HashMap<String, Runnable> dictionaryOfCommands;

  private void initDictionaryOfCommands() {
    dictionaryOfCommands = new HashMap<>();
    dictionaryOfCommands.put("add student", this::addStudent);
    dictionaryOfCommands.put("get student", this::getStudentById);
    dictionaryOfCommands.put("get lab problem", this::getLabProblemById);
    dictionaryOfCommands.put("get assignment", this::getAssignmentById);
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
  /** ro.ubb.UI method for printing the console menu */
  private void printMenu() {
    String menu = "";
    menu += "Menu options:" + System.lineSeparator();
    menu += "- add student" + System.lineSeparator();
    menu += "- get student" + System.lineSeparator();
    menu += "- print students" + System.lineSeparator();
    menu += "- print students sorted" + System.lineSeparator();
    menu += "- update student" + System.lineSeparator();
    menu += "- delete student" + System.lineSeparator();
    menu += "- filter students [by group]" + System.lineSeparator();
    menu += "- add lab problem" + System.lineSeparator();
    menu += "- get lab problem" + System.lineSeparator();
    menu += "- print lab problems" + System.lineSeparator();
    menu += "- print lab problems sorted" + System.lineSeparator();
    menu += "- update lab problem" + System.lineSeparator();
    menu += "- delete lab problem" + System.lineSeparator();
    menu += "- filter lab problems [by number]" + System.lineSeparator();
    menu += "- add assignment" + System.lineSeparator();
    menu += "- get assignment" + System.lineSeparator();
    menu += "- print assignments" + System.lineSeparator();
    menu += "- print assignments sorted" + System.lineSeparator();
    menu += "- update assignment" + System.lineSeparator();
    menu += "- delete assignment" + System.lineSeparator();
    menu += "- max mean student" + System.lineSeparator();
    menu += "- lab problem most" + System.lineSeparator();
    menu += "- avg grade" + System.lineSeparator();
    menu += "- student problems" + System.lineSeparator();
    menu += "- exit" + System.lineSeparator();
    System.out.println(menu);
  }

  /** Take specific user input and print server's answer to the call getAssignmentById call. */
  private void getAssignmentById() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return assignmentService.getAssignmentById(id).toString();
                } catch (IllegalArgumentException ex) {
                  return ex.getMessage();
                } catch (NullPointerException e) {
                  return "Failed to obtain";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

  private void getLabProblemById() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return labProblemService.getLabProblemById(id).toString();
                } catch (IllegalArgumentException ex) {
                  return ex.getMessage();
                } catch (NullPointerException e) {
                  return "Failed to obtain";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

  private void getStudentById() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return studentService.getStudentById(id).toString();
                } catch (IllegalArgumentException e) {
                  return e.getMessage();
                } catch (NullPointerException e) {
                  return "Failed to obtain student";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
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
          System.out.println("wrong input!");
          return;
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
      Sort finalSort = sort;
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return labProblemService.getAllLabProblemsSorted(finalSort).stream()
                      .map(LabProblem::toString)
                      .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
                } catch (ClassReflectionException | NullPointerException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);
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
          System.out.println("wrong input!");
          return;
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
      Sort finalSort = sort;
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return assignmentService.getAllAssignmentsSorted(finalSort).stream()
                      .map(Assignment::toString)
                      .reduce("", (s1, s2) -> s1 + "\n" + s2);
                } catch (ClassReflectionException | NullPointerException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);
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
          System.out.println("wrong input!");
          return;
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

      Sort finalSort = sort;
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  return studentService.getAllStudentsSorted(finalSort).stream()
                      .map(Student::toString)
                      .reduce("", (s1, s2) -> s1 + "\n" + s2);
                } catch (ClassReflectionException | NullPointerException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException e) {
      System.out.println("Invalid input!");
    } catch (ClassReflectionException e) {
      System.err.println(e.getMessage());
    }
  }

  private void studentProblems() {

    CompletableFuture.supplyAsync(
            () -> {
//              Student emptyStudent = new Student();
              try {
                return assignmentService.studentAssignedProblems().entrySet().stream()
                    .map(
                        entry ->
                            entry.getKey()
                                + System.lineSeparator()
                                + entry.getValue().stream()
                                    .map(LabProblem::toString)
                                    .reduce((s1, s2) -> s1 + System.lineSeparator() + s2)
                                    .orElse(""))
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (NullPointerException e) {
                return e.getMessage();
              }
            })
        .thenAcceptAsync(System.out::println);
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
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return "The mean of all assignments is " + assignmentService.averageGrade().toString();
              } catch (NullPointerException e) {
                return e.getMessage() + " assignments";
              }
            })
        .thenAcceptAsync(System.out::println);
  }

  private void labProblemMostAssigned() {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                AbstractMap.SimpleEntry<Long, Long> result =
                    assignmentService.idOfLabProblemMostAssigned();
                return "lab problem most assigned id: "
                    + result.getKey()
                    + " - "
                    + result.getValue()
                    + "times";
              } catch (NullPointerException e) {
                return e.getMessage() + "\nno lab problems assigned";
              }
            })
        .thenAcceptAsync(System.out::println);
  }

  private void greatestMeanOfStudent() {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                AbstractMap.SimpleEntry<Long, Double> result = assignmentService.greatestMean();
                return "The greatest mean is of student id = "
                    + result.getKey()
                    + ": "
                    + result.getValue();
              } catch (NullPointerException e) {
                return e.getMessage() + "\nno students or assignments";
              }
            })
        .thenAcceptAsync(System.out::println);
  }

  /** ro.ubb.Main loop of the user interface */
  public void run() {
    initDictionaryOfCommands();
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

    CompletableFuture.supplyAsync(
            () -> {
              try {
                return assignmentService.getAllAssignments().stream()
                    .map(Assignment::toString)
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (NullPointerException e) {
                return e.getMessage().substring(e.getMessage().indexOf(" ") + 1);
              }
            })
        .thenAcceptAsync(System.out::println);
  }

  private void addAssignment() {
    System.out.println("Read assignment {id, studentId, labProblemId. grade}");
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
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  if (assignmentService.addAssignment(id, studentId, labProblemId, grade) == null)
                    return "Assignment added";
                  return "Assignment not added, assignment with that id is already in the database";
                } catch (ValidatorException | RepositoryException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

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

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  if (studentService.addStudent(id, serialNumber, name, group) == null)
                    return "Student not added, already in database";
                  return "Student added";
                } catch (ValidatorException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (ValidatorException ex) {
      System.out.println(ex.getMessage());
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }
  /** ro.ubb.UI method for printing all students */
  private void printStudents() {
    System.out.println(studentService.getAllStudents().size());
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return studentService.getAllStudents().stream()
                    .map(Student::toString)
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (NullPointerException e) {
                return "Getting students error";
              }
            })
        .thenAcceptAsync(System.out::println);
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
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  if (labProblemService.addLabProblem(id, problemNumber, description) == null)
                    return "Lab problem added";
                  return "Lab problem not added, already in the database the entity with that id";
                } catch (ValidatorException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  /** ro.ubb.UI method for printing all lab problems */
  private void printLabProblems() {
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return labProblemService.getAllLabProblems().stream()
                    .map(LabProblem::toString)
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (NullPointerException e) {
                return e.getMessage().substring(e.getMessage().indexOf(" ") + 1);
              }
            })
        .thenAcceptAsync(System.out::println);
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
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  labProblemService.updateLabProblem(id, problemNumber, description);
                  return "Update method completed";
                } catch (ValidatorException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

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
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  labProblemService.deleteLabProblem(id);
                  return "Delete method completed";
                } catch (IllegalArgumentException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
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
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return labProblemService.filterByProblemNumber(problemNumber).stream()
                    .map(LabProblem::toString)
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (IllegalArgumentException ex) {
                return ex.getMessage();
              }
            })
        .thenAcceptAsync(System.out::println);
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

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  studentService.updateStudent(id, serialNumber, name, group);
                  return "Update method finished";
                } catch (ValidatorException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);
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

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  studentService.deleteStudent(id);
                  return "Delete method finished";
                } catch (IllegalArgumentException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
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
    CompletableFuture.supplyAsync(
            () -> {
              try {
                return studentService.filterByGroup(groupNumber).stream()
                    .map(Student::toString)
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2);
              } catch (IllegalArgumentException ex) {
                return ex.getMessage();
              }
            })
        .thenAcceptAsync(System.out::println);
  }

  private void updateAssignment() {
    System.out.println("Update assignment {id, studentId, labProblemId, grade}");
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

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  assignmentService.updateAssignment(id, studentId, labProblemId, grade);
                  return "Update method finished";
                } catch (ValidatorException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);
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
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  assignmentService.deleteAssignment(id);
                  return "Delete method finished";
                } catch (IllegalArgumentException ex) {
                  return ex.getMessage();
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.err.println("bad input");
    }
  }
}
