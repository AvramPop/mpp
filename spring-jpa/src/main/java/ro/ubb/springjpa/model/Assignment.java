package ro.ubb.springjpa.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class Assignment extends BaseEntity<Long> {
  private Long studentId;
  private Long labProblemId;
  private int grade;

  public Assignment() {}

  public Assignment(Long studentId, Long labProblemId, int grade) {
    this.studentId = studentId;
    this.labProblemId = labProblemId;
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "Assignment{ "
        + "id= "
        + getId()
        + ", studentId="
        + studentId
        + ", labProblemId="
        + labProblemId
        + ", grade="
        + grade
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Assignment that = (Assignment) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, labProblemId, grade);
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Long getLabProblemId() {
    return labProblemId;
  }

  public void setLabProblemId(Long labProblemId) {
    this.labProblemId = labProblemId;
  }

  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }
}
