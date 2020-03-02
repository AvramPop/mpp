package domain;

import java.util.Objects;

/** A LabProblem having a number (positive integer) and a description (nonempty). */
public class LabProblem extends BaseEntity<Long> {
  private int problemNumber;
  private String description;

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
        + "problemNumber="
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
    return problemNumber == that.problemNumber && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(problemNumber, description);
  }
}
