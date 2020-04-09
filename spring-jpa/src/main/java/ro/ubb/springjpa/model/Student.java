package ro.ubb.springjpa.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Student extends BaseEntity<Long> {
  private String serialNumber;
  private String name;
  private int studentGroup;

  public Student() {
    serialNumber = "";
    name = "";
    studentGroup = -1;
    setId(-1L);
  }

  public Student(String serialNumber, String name, int studentGroup) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.studentGroup = studentGroup;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStudentGroup() {
    return studentGroup;
  }

  public void setStudentGroup(int group) {
    this.studentGroup = group;
  }

  @Override
  public String toString() {
    return "Student{"
        + "id="
        + getId()
        + ", serialNumber='"
        + serialNumber
        + '\''
        + ", name='"
        + name
        + '\''
        + ", group="
        + studentGroup
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return getId().equals(student.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(serialNumber, name, studentGroup);
  }
}
