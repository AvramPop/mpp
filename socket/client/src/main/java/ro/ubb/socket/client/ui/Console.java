package ro.ubb.socket.client.ui;

import ro.ubb.socket.client.service.AssignmentClientService;
import ro.ubb.socket.client.service.LabProblemClientService;
import ro.ubb.socket.client.service.StudentClientService;
import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ClassReflectionException;
import ro.ubb.socket.common.domain.exceptions.RepositoryException;
import ro.ubb.socket.common.domain.exceptions.ServiceException;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.sort.Sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/** Console based user interface */
public class Console {
  private StudentClientService studentService;
  private LabProblemClientService labProblemService;
  private AssignmentClientService assignmentService;
  private HashMap<String, Runnable> dictionaryOfCommands;
  private ExecutorService executorService;

  public Console(
      StudentClientService studentService,
      LabProblemClientService labProblemService,
      AssignmentClientService assignmentService,
      ExecutorService executorService) {
    this.studentService = studentService;
    this.labProblemService = labProblemService;
    this.assignmentService = assignmentService;
    this.executorService = executorService;
    // I use lambda methods with a hash table to not to make if statements
    // if the thing fails it gets a null pointer exception
    // which means not a valid command
    initDictionaryOfCommands();
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
    menu += "- add lab problem" + System.lineSeparator();
    menu += "- get lab problem" + System.lineSeparator();
    menu += "- print lab problems" + System.lineSeparator();
    menu += "- print lab problems sorted" + System.lineSeparator();
    menu += "- update lab problem" + System.lineSeparator();
    menu += "- delete lab problem" + System.lineSeparator();
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
    menu += "- shutdown server" + System.lineSeparator();
    menu += "- exit" + System.lineSeparator();
    System.out.println(menu);
    /*
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
       */
  }

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
    dictionaryOfCommands.put("update student", this::updateStudent);
    dictionaryOfCommands.put("delete student", this::deleteStudent);
    dictionaryOfCommands.put("add assignment", this::addAssignment);
    dictionaryOfCommands.put("print assignments", this::printAssignments);
    dictionaryOfCommands.put("delete assignment", this::deleteAssignment);
    dictionaryOfCommands.put("update assignment", this::updateAssignment);
    dictionaryOfCommands.put("max mean student", this::greatestMeanOfStudent);
    dictionaryOfCommands.put("lab problem most", this::labProblemMostAssigned);
    dictionaryOfCommands.put("avg grade", this::averageGrade);
    dictionaryOfCommands.put("student problems", this::studentProblems);
    dictionaryOfCommands.put("shutdown server", this::shutDownServer);
    dictionaryOfCommands.put("exit", () -> System.exit(0));
  }

  private void getAssignmentById() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());

      Future<Assignment> assignmentFuture = assignmentService.getAssignmentById(id);
      executorService.submit(
          () -> {
            try {
              System.out.println(assignmentFuture.get());
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Failed to obtain");
            }
          });

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

      Future<LabProblem> labProblemFuture = labProblemService.getLabProblemById(id);
      executorService.submit(
          () -> {
            try {
              System.out.println(labProblemFuture.get());
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Failed to obtain");
            }
          });

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

      Future<Student> studentFuture = studentService.getStudentById(id);
      executorService.submit(
          () -> {
            try {
              System.out.println(studentFuture.get());
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Failed to obtain");
            }
          });

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

  private void shutDownServer() {
    assignmentService.shutDownServer();
    System.exit(0);
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
      Future<List<LabProblem>> labProblems = labProblemService.getAllLabProblemsSorted(sort);
      executorService.submit(
          () -> {
            try {
              labProblems.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
              e.printStackTrace();
            }
          });
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
      Future<List<Assignment>> assignments = assignmentService.getAllAssignmentsSorted(sort);
      executorService.submit(
          () -> {
            try {
              assignments.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage());
            }
          });
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
      Future<List<Student>> students = studentService.getAllStudentsSorted(sort);

      executorService.submit(
          () -> {
            try {
              students.get().forEach(System.out::println);
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage());
            }
          });

    } catch (IOException e) {
      System.out.println("Invalid input!");
    } catch (ClassReflectionException e) {
      System.err.println(e.getMessage());
    }
  }

  private void studentProblems() {
    Future<Map<Student, List<LabProblem>>> studentsLabProblems =
        assignmentService.studentAssignedProblems();

    executorService.submit(
        () -> {
          Student emptyStudent = new Student();
          try {
            for (Map.Entry<Student, List<LabProblem>> entry :
                studentsLabProblems.get().entrySet()) {
              if (!entry.getKey().getSerialNumber().equals("")) {
                System.out.println(entry.getKey().toString());
                System.out.println("Problems:");
                entry.getValue().forEach(labProblem -> System.out.println(labProblem.toString()));
              }
            }
            //            System.out.println("Unused problems:");
            //            studentsLabProblems.get().get(emptyStudent).forEach(System.out::println);
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
          }
        });
  }

  private void averageGrade() {
    Future<Double> mean = assignmentService.averageGrade();
    executorService.submit(
        () -> {
          try {
            System.out.println("The mean of all assignments is " + mean.get());
          } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
            System.err.println("assignments");
          }
        });
  }

  private void labProblemMostAssigned() {
    Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned =
        assignmentService.idOfLabProblemMostAssigned();
    executorService.submit(
        () -> {
          try {
            System.out.println(
                "lab problem most assigned id: "
                    + idOfLabProblemMostAssigned.get().getKey()
                    + " - "
                    + idOfLabProblemMostAssigned.get().getValue()
                    + "times");
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            System.err.println("no lab problems assigned");
          }
        });
  }

  private void greatestMeanOfStudent() {
    Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean = assignmentService.greatestMean();

    executorService.submit(
        () -> {
          try {
            System.out.println(
                "The greatest mean is of student id = "
                    + greatestMean.get().getKey()
                    + ": "
                    + greatestMean.get().getValue());
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage());
            System.err.println("no students or assignments");
          }
        });
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
      } catch (ConnectException | ServiceException ex) {
        System.out.println(ex.getMessage());
      } catch (IOException ex) {
        System.out.println("Error with input!");
      } catch (NullPointerException ex) {
        System.out.println("Not a vaild comand");
      }
    }
  }

  private void printAssignments() {

    Future<Set<Assignment>> students = assignmentService.getAllAssignments();
    executorService.submit(
        () -> {
          try {
            students.get().forEach(System.out::println);
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
          }
        });
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
      Future result = assignmentService.addAssignment(id, studentId, labProblemId, grade);
      executorService.submit(
          () -> {
            try {
              result.get();
              System.out.println("Assignment added");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Assignment not added");
            }
          });

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
      Future<Boolean> studentFuture = studentService.addStudent(id, serialNumber, name, group);

      executorService.submit(
          () -> {
            try {
              studentFuture.get();
              System.out.println("Student added");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Student not added");
            }
          });

    } catch (ValidatorException ex) {
      System.out.println(ex.getMessage());
    } catch (IOException | NumberFormatException e) {
      //      e.printStackTrace();
      System.out.println("invalid input");
    }
  }
  /** ro.ubb.UI method for printing all students */
  private void printStudents() {
    Future<Set<Student>> students = studentService.getAllStudents();
    executorService.submit(
        () -> {
          try {
            students.get().forEach(System.out::println);
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
          }
        });
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
      Future<Boolean> labProblemFuture =
          labProblemService.addLabProblem(id, problemNumber, description);
      executorService.submit(
          () -> {
            try {
              labProblemFuture.get();
              System.out.println("Lab problem added");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Lab problem not added");
            }
          });

    } catch (ValidatorException e) {
      System.err.println(e.getMessage());
    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  /** ro.ubb.UI method for printing all lab problems */
  private void printLabProblems() {
    Future<Set<LabProblem>> labProblems = labProblemService.getAllLabProblems();
    executorService.submit(
        () -> {
          try {
            labProblems.get().forEach(System.out::println);
          } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
          }
        });
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
      Future<Boolean> labProblemFuture =
          labProblemService.updateLabProblem(id, problemNumber, description);

      executorService.submit(
          () -> {
            try {
              labProblemFuture.get();
              System.out.println("Lab Problem updated");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Lab Problem not updated");
            }
          });

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

      Future<Boolean> labProblemDeleteFuture = labProblemService.deleteLabProblem(id);
      executorService.submit(
          () -> {
            try {
              labProblemDeleteFuture.get();
              System.out.println("Delete successful");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Delete failed");
            }
          });

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  //    /** ro.ubb.UI method filters lab problems by problem number */
  //    private void filterLabProblemsByProblemNumber() {
  //      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
  //      int problemNumber;
  //      try {
  //        System.out.println("Enter problem number: ");
  //        problemNumber = Integer.parseInt(input.readLine().strip());
  //
  //      } catch (IOException | NumberFormatException ex) {
  //        System.out.println("Invalid input!");
  //        return;
  //      }
  //
  //      Set<LabProblem> labProblems = labProblemService.filterByProblemNumber(problemNumber);
  //      labProblems.forEach(System.out::println);
  //    }

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

      Future<Boolean> labProblemFuture =
          studentService.updateStudent(id, serialNumber, name, group);
      executorService.submit(
          () -> {
            try {
              labProblemFuture.get();
              System.out.println("Student updated");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Student not updated");
            }
          });
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

      Future<Boolean> studentFuture = studentService.deleteStudent(id);
      executorService.submit(
          () -> {
            try {
              studentFuture.get();
              System.out.println("Delete successful");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Delete failed!");
            }
          });

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }
  //    /** ro.ubb.UI method filters students by group number */
  //    private void filterStudentsByGroup() {
  //      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
  //      int groupNumber;
  //      try {
  //        System.out.println("Enter group number: ");
  //        groupNumber = Integer.parseInt(input.readLine().strip());
  //
  //      } catch (IOException | NumberFormatException ex) {
  //        System.out.println("Invalid input!");
  //        return;
  //      }
  //
  //      Set<Student> students = studentService.filterByGroup(groupNumber);
  //      students.forEach(System.out::println);
  //    }

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
      Future<Boolean> assignmentFuture =
          assignmentService.updateAssignment(id, studentId, labProblemId, grade);

      executorService.submit(
          () -> {
            try {
              assignmentFuture.get();
              System.out.println("Assignment updated");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Assignment not updated");
            }
          });
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
      Future<Boolean> assignmentFuture = assignmentService.deleteAssignment(id);
      executorService.submit(
          () -> {
            try {
              assignmentFuture.get();
              System.out.println("Assignment deleted");
            } catch (InterruptedException | ExecutionException e) {
              System.out.println(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
              System.out.println("Assignment not deleted");
            }
          });
    } catch (IOException | NumberFormatException e) {
      System.err.println("bad input");
    }
  }
}
