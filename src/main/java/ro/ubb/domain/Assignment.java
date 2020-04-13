package ro.ubb.domain;

import jdk.jfr.Name;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table(name="assignments")
@AttributeOverrides({
        @AttributeOverride(name="id",column = @Column(name = "assignment_id"))
})
public class Assignment extends BaseEntity<Long> {

  @ManyToOne(optional = false,fetch = FetchType.LAZY)
  @JoinColumn(name="student_id")
  private Student student;

  @ManyToOne(optional = false,fetch = FetchType.LAZY)
  @JoinColumn(name="lab_problem_id")
  private LabProblem labProblem;

  @Column(name="grade")
  @Min(0)
  @Max(10)
  private Integer grade;

  public Student getStudent() {
    System.out.println("Hello");
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public LabProblem getLabProblem() {
    return labProblem;
  }

  public void setLabProblem(LabProblem labProblem) {
    this.labProblem = labProblem;
  }


  public Assignment() {}

  public Assignment(Student student, LabProblem labProblem) {
    this.student = student;
    this.labProblem = labProblem;
  }

  public Assignment(Student student, LabProblem labProblem, int grade) {
    this.student = student;
    this.labProblem = labProblem;
    this.grade = grade;
  }

  @Override
  public String toString() {
    return "Assignment{ "
        + "id= "
        + getId()
        + ", studentId="
        + student.getId()
        + ", labProblemId="
        + labProblem.getId()
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
    return Objects.hash(student.getId(), labProblem.getId(), grade);
  }



  public int getGrade() {
    return grade;
  }

  public void setGrade(int grade) {
    this.grade = grade;
  }

}
