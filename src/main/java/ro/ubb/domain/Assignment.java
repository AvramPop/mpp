package ro.ubb.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Assignment extends BaseEntity<Long> {

    Long studentId;
    Long labProblemId;

    public Assignment(Long studentId, Long labProblemId) {
        this.studentId = studentId;
        this.labProblemId = labProblemId;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "studentId=" + studentId +
                ", labProblemId=" + labProblemId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, labProblemId);
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


    @Override
    public String objectToFileLine(String delimiter) {
        return this.getId() + delimiter + this.studentId + delimiter + this.labProblemId;
    }


}
