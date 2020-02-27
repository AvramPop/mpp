package UI;

import domain.LabProblem;
import domain.Student;
import domain.exceptions.ValidatorException;
import service.LabProblemService;
import service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class Console {
  StudentService studentController;
  LabProblemService lapProblemController;

  Console(StudentService studentController, LabProblemService lapProblemController) {
    this.studentController = studentController;
    this.lapProblemController = lapProblemController;
  }

  public void run() {
    while (true) {}
  }

  private void addStudent() {
    try {
      Student newStudent = readStudent();
      if (newStudent == null) {
        System.out.println("Invalid input! ID must be positive number");
        return;
      }
      studentController.addStudent(newStudent);
    } catch (ValidatorException ex) {
      ex.printStackTrace();
      System.out.println(ex);
    }
  }

  private void addLabProblem() {
    try {

    } catch (ValidatorException ex) {
      ex.printStackTrace();
      System.out.println(ex);
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

  private Student readStudent() {
    System.out.println("Read student {id,serialNumber, name, group}");

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    Student newStudent = null;
    try {
      long id = Long.valueOf(input.readLine());
      String serialNumber = input.readLine();
      String name = input.readLine();
      int group = Integer.parseInt(input.readLine());
      newStudent = new Student(serialNumber, name, group);
      newStudent.setId(id);
    } catch (IOException | NumberFormatException ex) {
      ex.printStackTrace();
    }
    return newStudent;
  }
}
