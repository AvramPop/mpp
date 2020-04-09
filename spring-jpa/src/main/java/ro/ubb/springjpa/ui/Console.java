package ro.ubb.springjpa.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.springjpa.exceptions.ClassReflectionException;
import ro.ubb.springjpa.exceptions.RepositoryException;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.Assignment;
import ro.ubb.springjpa.model.LabProblem;
import ro.ubb.springjpa.model.Student;
import ro.ubb.springjpa.service.AssignmentService;
import ro.ubb.springjpa.service.LabProblemService;
import ro.ubb.springjpa.service.StudentService;
import ro.ubb.springjpa.service.sort.Sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/** Created by radu. */
@Component
public class Console {
  @Autowired private StudentService studentService;
  @Autowired private LabProblemService labProblemService;
  @Autowired private AssignmentService assignmentService;
  private HashMap<String, Runnable> dictionaryOfCommands;

  public Console() {
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
          Student emptyStudent = new Student();
            return assignmentService.studentAssignedProblems().map(
                map -> map.entrySet().stream()
                    .map(
                        entry ->
                            entry.getKey()
                                + System.lineSeparator()
                                + entry.getValue().stream()
                                .map(LabProblem::toString)
                                .reduce((s1, s2) -> s1 + System.lineSeparator() + s2)
                                .orElse(""))
                    .reduce("", (s1, s2) -> s1 + System.lineSeparator() + s2)).orElse("");
        })
        .thenAcceptAsync(System.out::println);
    }

  private void averageGrade() {
    CompletableFuture.supplyAsync(
        () -> {
          try {
            return "The mean of all assignments is " + assignmentService.averageGrade().get();
          } catch (NoSuchElementException e) {
            return "bad request for assignments";
          }
        })
        .thenAcceptAsync(System.out::println);
  }

  private void labProblemMostAssigned() {
    CompletableFuture.supplyAsync(
        () -> {
          try {
            AbstractMap.SimpleEntry<Long, Long> result =
                assignmentService.idOfLabProblemMostAssigned().get();
            return "lab problem most assigned id: "
                + result.getKey()
                + " - "
                + result.getValue()
                + "times";
          } catch (NoSuchElementException e) {
            return e.getMessage() + "\nno lab problems assigned";
          }
        })
        .thenAcceptAsync(System.out::println);
  }

  private void greatestMeanOfStudent() {
    CompletableFuture.supplyAsync(
        () -> {
          try {
            AbstractMap.SimpleEntry<Long, Double> result = assignmentService.greatestMean().get();
            return "The greatest mean is of student id = "
                + result.getKey()
                + ": "
                + result.getValue();
          } catch (NoSuchElementException e) {
            return e.getMessage() + "\nno students or assignments";
          }
        })
        .thenAcceptAsync(System.out::println);
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
      CompletableFuture.supplyAsync(
          () -> {
            try {
              if (assignmentService.addAssignment(id, studentId, labProblemId, grade).isEmpty())
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
              if (studentService.addStudent(id, serialNumber, name, group).isEmpty())
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
      //      e.printStackTrace();
      System.out.println("invalid input");
    }
  }
  /** ro.ubb.UI method for printing all students */
  private void printStudents() {
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
              if (labProblemService.addLabProblem(id, problemNumber, description).isEmpty())
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
                  if (labProblemService
                      .updateLabProblem(id, problemNumber, description)
                      .isEmpty()) {
                    return "Update method completed";
                  } else return "update failed";
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
                  if (labProblemService.deleteLabProblem(id).isPresent()) {
                    return "Delete method completed";
                  } else return "delete failed";
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
                  if (studentService.updateStudent(id, serialNumber, name, group).isEmpty()) {
                    return "Update method finished";
                  } else return "update failed";
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
              if(studentService.deleteStudent(id).isPresent()){

                return "Delete method finished";
              } else return "bad delete";
            } catch (IllegalArgumentException ex) {
              return ex.getMessage();
            }
          })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
      return;
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

      CompletableFuture.supplyAsync(
              () -> {
                try {
                  if (assignmentService
                      .updateAssignment(id, studentId, labProblemId, grade)
                      .isEmpty()) {
                     return "Update method finished";
                  } else return "bad update";
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
                  if (assignmentService.deleteAssignment(id).isPresent()) {
                    return "Delete method finished";
                  } else return "bad delete";
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
