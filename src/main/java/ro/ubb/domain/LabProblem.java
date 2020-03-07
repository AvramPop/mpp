package ro.ubb.domain;

import java.util.Arrays;
import java.util.List;
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
    return problemNumber == that.problemNumber && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(problemNumber, description);
  }

//  @Override
//  public LabProblem objectFromFileLine(String fileLine, String delimiter) {
//
//    String[] components = fileLine.split(";");
//    LabProblem newEntity = new LabProblem(Integer.parseInt(components[1]), components[2]);
//    newEntity.setId(Long.parseLong(components[0]));
//    return newEntity;
//  }

  @Override
  public String objectToFileLine(String delimiter) {
    return this.getId() + delimiter + this.problemNumber + delimiter + this.description;
  }



  public static ObjectFromFileLine<LabProblem> objectFromFileLine() {
    return (line, delimiter) -> {
      List<String> params = Arrays.asList(line.split(delimiter));
      LabProblem labProblem =
          new LabProblem(Integer.parseInt(params.get(1)), params.get(2));
      labProblem.setId(Long.parseLong(params.get(0)));
      return labProblem;
    };
  }
}
