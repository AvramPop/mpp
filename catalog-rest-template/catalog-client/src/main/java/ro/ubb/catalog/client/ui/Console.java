package ro.ubb.catalog.client.ui;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ro.ubb.catalog.web.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class Console {
  private HashMap<String, Runnable> dictionaryOfCommands;
  private static final String baseURL = "http://localhost:8082/api";
  private final RestTemplate restTemplate;

  public Console() {
    initDictionaryOfCommands();
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("ro.ubb.catalog.client.config");
    restTemplate = context.getBean(RestTemplate.class);
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
    dictionaryOfCommands.put("exit", () -> System.exit(0));
  }

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

  public void run() {
    while (true) {
      printMenu();
      BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
      try {
        String inputString = input.readLine();
        dictionaryOfCommands.get(inputString).run();
      } catch (IOException ex) {
        System.out.println("Error with input!");
      } catch (NullPointerException ex) {
        System.out.println("Not a vaild comand");
      }
    }
  }

  private void averageGrade() {
    CompletableFuture.supplyAsync(
            () ->
                "The mean of all assignments is "
                    + restTemplate.getForObject(baseURL + "/logic/avg", DoubleDto.class).getValue())
        .thenAcceptAsync(System.out::println);
  }

  private void greatestMeanOfStudent() {
    CompletableFuture.supplyAsync(
            () -> {
              PairDto<Double, Long> result =
                  restTemplate.getForObject(baseURL + "/logic/mean", PairDto.class);
              return "The greatest mean is "
                  + result.getKey()
                  + " of student with id = "
                  + result.getValue();
            })
        .thenAcceptAsync(System.out::println);
  }

  private void labProblemMostAssigned() {
    CompletableFuture.supplyAsync(
            () -> {
              PairDto<Long, Long> result =
                  restTemplate.getForObject(baseURL + "/logic/assigned", PairDto.class);
              return "The problem most assigned has id =  "
                  + result.getKey()
                  + ", times: "
                  + result.getValue();
            })
        .thenAcceptAsync(System.out::println);
  }

  private void updateAssignment() {
    System.out.println("Read assignment {id, studentId, labProblemId, grade}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter student id: ");
      long studentId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter lab problem id: ");
      long labProblemId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter grade: ");
      int grade = Integer.parseInt(input.readLine().strip());
      CompletableFuture.supplyAsync(
              () -> {
                AssignmentDto updatedAssignment = new AssignmentDto(studentId, labProblemId, grade);
                updatedAssignment.setId(id);
                try {
                  restTemplate.put(
                      baseURL + "/assignments/{id}", updatedAssignment, updatedAssignment.getId());
                  return "Assignment updated";
                } catch (RestClientException e) {
                  return "Assignment not updated";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
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
                  restTemplate.delete(baseURL + "/assignments/{id}", id);
                  return "successful delete";
                } catch (RestClientException ex) {
                  return "delete failed";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

  private void addAssignment() {
    System.out.println("Read assignment {id, studentId, labProblemId, grade}");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Enter id: ");
      long id = Long.parseLong(input.readLine().strip());
      System.out.println("Enter student id: ");
      long studentId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter lab problem id: ");
      long labProblemId = Long.parseLong(input.readLine().strip());
      System.out.println("Enter grade: ");
      int grade = Integer.parseInt(input.readLine().strip());
      CompletableFuture.supplyAsync(
              () -> {
                AssignmentDto newAssignment = new AssignmentDto(studentId, labProblemId, grade);
                newAssignment.setId(id);
                try {
                  restTemplate.postForEntity(
                      baseURL + "/assignments", newAssignment, AssignmentDto.class);
                  return "Assignment added";
                } catch (RestClientException e) {
                  return "Assignment not added";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }

  private void printAssignments() {
    CompletableFuture.supplyAsync(
            () -> restTemplate.getForObject(baseURL + "/assignments", AssignmentsDto.class))
        .thenAcceptAsync(result -> result.getAssignments().forEach(System.out::println));
  }

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
            () ->
                restTemplate.getForObject(
                    baseURL + "/students/group/{groupNumber}", StudentsDto.class, groupNumber))
        .thenAcceptAsync(result -> result.getStudents().forEach(System.out::println));
  }

  private void deleteStudent() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  restTemplate.delete(baseURL + "/students/{id}", id);
                  return "successful delete";
                } catch (RestClientException ex) {
                  return "delete failed";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

  private void updateStudent() {
    System.out.println("Update student {id, serialNumber, name, group}");
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
                StudentDto updatedStudent = new StudentDto(serialNumber, name, group);
                updatedStudent.setId(id);
                try {
                  restTemplate.put(
                      baseURL + "/students/{id}", updatedStudent, updatedStudent.getId());
                  return "Student updated";
                } catch (RestClientException e) {
                  return "Student not updated";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }

  private void deleteLabProblem() {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    long id;
    try {
      System.out.println("Enter id: ");
      id = Long.parseLong(input.readLine().strip());
      CompletableFuture.supplyAsync(
              () -> {
                try {
                  restTemplate.delete(baseURL + "/labs/{id}", id);
                  return "successful delete";
                } catch (RestClientException ex) {
                  return "delete failed";
                }
              })
          .thenAcceptAsync(System.out::println);

    } catch (IOException | NumberFormatException ex) {
      System.out.println("Invalid input!");
    }
  }

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
            () ->
                restTemplate.getForObject(
                    baseURL + "/labs/bynumber/{problemNumber}",
                    LabProblemsDto.class,
                    problemNumber))
        .thenAcceptAsync(result -> result.getLabProblems().forEach(System.out::println));
  }

  private void updateLabProblem() {
    System.out.println("Read lab problem {id, problem-number, description}");
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
                LabProblemDto updatedLabProblem = new LabProblemDto(problemNumber, description);
                updatedLabProblem.setId(id);
                try {
                  restTemplate.put(
                      baseURL + "/labs/{id}", updatedLabProblem, updatedLabProblem.getId());
                  return "Lab problem updated";
                } catch (RestClientException e) {
                  return "Lab problem not updated";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }

  private void printLabProblems() {
    CompletableFuture.supplyAsync(
            () -> restTemplate.getForObject(baseURL + "/labs", LabProblemsDto.class))
        .thenAcceptAsync(result -> result.getLabProblems().forEach(System.out::println));
  }

  private void addLabProblem() {
    System.out.println("Read lab problem {id, problem-number, description}");
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
                LabProblemDto newLabProblem = new LabProblemDto(problemNumber, description);
                newLabProblem.setId(id);
                try {
                  restTemplate.postForEntity(baseURL + "/labs", newLabProblem, LabProblemDto.class);
                  return "Lab problem added";
                } catch (RestClientException e) {
                  return "Lab problem not added";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }

  private void printLabProblemsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    var ref =
        new Object() {
          SortDto sortDto = null;
        };
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        String sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = "ASC";
        } else if (order.equals("DESC")) {
          sortingDirection = "DESC";
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (ref.sortDto == null) {
          ref.sortDto = new SortDto();
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
          ref.sortDto.setClassName("LabProblem");
        } else {
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
        }
      }
      System.out.println(ref.sortDto.toString());
      CompletableFuture.supplyAsync(
              () ->
                  restTemplate.postForObject(
                      baseURL + "/labs/sorted", ref.sortDto, LabProblemsDto.class))
          .thenAcceptAsync(result -> result.getLabProblems().forEach(System.out::println));
    } catch (IOException e) {
      System.out.println("Invalid input!");
    }
  }

  private void printAssignmentsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    var ref =
        new Object() {
          SortDto sortDto = null;
        };
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        String sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = "ASC";
        } else if (order.equals("DESC")) {
          sortingDirection = "DESC";
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (ref.sortDto == null) {
          ref.sortDto = new SortDto();
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
          ref.sortDto.setClassName("Assignment");
        } else {
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
        }
      }
      System.out.println(ref.sortDto.toString());
      CompletableFuture.supplyAsync(
          () ->
              restTemplate.postForObject(
                  baseURL + "/assignments/sorted", ref.sortDto, AssignmentsDto.class))
          .thenAcceptAsync(result -> result.getAssignments().forEach(System.out::println));
    } catch (IOException e) {
      System.out.println("Invalid input!");
    }
  }

  private void printStudentsSorted() {
    System.out.println("Sort by criteria: <order {ASC/ DESC} column-name>. 'done' when done");
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    var ref =
        new Object() {
          SortDto sortDto = null;
        };
    try {
      while (true) {
        System.out.println("order: ");
        String order = input.readLine().strip();
        if (order.equals("done")) break;
        String sortingDirection;
        if (order.equals("ASC")) {
          sortingDirection = "ASC";
        } else if (order.equals("DESC")) {
          sortingDirection = "DESC";
        } else {
          System.err.println("wrong input!");
          break;
        }
        System.out.println("column-name:");
        String columnName = input.readLine().strip();
        if (ref.sortDto == null) {
          ref.sortDto = new SortDto();
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
          ref.sortDto.setClassName("Student");
        } else {
          ref.sortDto.getKeys().add(sortingDirection);
          ref.sortDto.getValues().add(columnName);
        }
      }
      System.out.println(ref.sortDto.toString());
      CompletableFuture.supplyAsync(
          () ->
              restTemplate.postForObject(
                  baseURL + "/students/sorted", ref.sortDto, StudentsDto.class))
          .thenAcceptAsync(result -> result.getStudents().forEach(System.out::println));
    } catch (IOException e) {
      System.out.println("Invalid input!");
    }
  }

  private void printStudents() {
    CompletableFuture.supplyAsync(
            () -> restTemplate.getForObject(baseURL + "/students", StudentsDto.class))
        .thenAcceptAsync(result -> result.getStudents().forEach(System.out::println));
  }

  private void addStudent() {
    System.out.println("Read student {id, serialNumber, name, group}");
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
                StudentDto newStudent = new StudentDto(serialNumber, name, group);
                newStudent.setId(id);
                try {
                  restTemplate.postForEntity(baseURL + "/students", newStudent, StudentDto.class);
                  return "Student added";
                } catch (RestClientException e) {
                  return "Student not added";
                }
              })
          .thenAcceptAsync(System.out::println);
    } catch (IOException | NumberFormatException e) {
      System.out.println("invalid input");
    }
  }
}
