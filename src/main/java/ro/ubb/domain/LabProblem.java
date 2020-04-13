package ro.ubb.domain;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;

/** A LabProblem having a number (positive integer) and a description (nonempty). */
@Entity
@Table(name="lab_problems")
@AttributeOverrides({
        @AttributeOverride(name="id",column = @Column(name = "lab_problem_id"))
})
public class LabProblem extends BaseEntity<Long> {
  @Column(name="problem_number")
  @NotEmpty
  private int problemNumber;
  @Column(name="description")
  @NotEmpty
  private String description;
  @OneToMany(mappedBy = "labProblem",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Assignment> assignments = new HashSet<>();

  public LabProblem() {
    this.problemNumber = -1;
    this.description = "";
    setId(-1L);
  }

  public LabProblem(int problemNumber, String description) {
    this.problemNumber = problemNumber;
    this.description = description;
  }

  public Set<Student> getStudents(){
    return assignments.stream()
            .map(Assignment::getStudent).collect(Collectors.toUnmodifiableSet());
  }
  public void addStudent(Student student){
    assignments.add(new Assignment(student,this));
  }

  public int getProblemNumber() {
    return problemNumber;
  }

  public void setProblemNumber(int problemNumber) {
    this.problemNumber = problemNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "LabProblem{"
        + "id="
        + getId()
        + ", problemNumber="
        + problemNumber
        + ", description='"
        + description
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LabProblem that = (LabProblem) o;
    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(problemNumber, description);
  }


}
