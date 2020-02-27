package domain;

import java.util.Objects;

/**
 * Entity representing the student group must be positive name must not be empty serialNumber must
 * not be empty
 */
public class Student extends BaseEntity<Long> {
  private String serialNumber;
  private String name;
  private int group;

  public Student() {}

  public Student(String serialNumber, String name, int group) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.group = group;
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

  public int getGroup() {
    return group;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  @Override
  public String toString() {
    return "Student{"
        + "serialNumber='"
        + serialNumber
        + '\''
        + ", name='"
        + name
        + '\''
        + ", group="
        + group
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Student student = (Student) o;
    return group == student.group
        && Objects.equals(serialNumber, student.serialNumber)
        && Objects.equals(name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serialNumber, name, group);
  }
}
