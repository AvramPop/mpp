package ro.ubb.socket.common.domain;

import java.util.Objects;

/** A LabProblem having a number (positive integer) and a description (nonempty). */
public class LabProblem extends BaseEntity<Long> {
  private int problemNumber;
  private String description;

  public LabProblem() {
    this.problemNumber = -1;
    this.description = "";
    setId(-1L);
  }

  public LabProblem(int problemNumber, String description) {
    this.problemNumber = problemNumber;
    this.description = description;
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
  public String objectToFileLine() {
    String delimiter = ",";
    return this.getId() + delimiter + this.problemNumber + delimiter + this.description;
  }

  @Override
  public int hashCode() {
    return Objects.hash(problemNumber, description);
  }
}
