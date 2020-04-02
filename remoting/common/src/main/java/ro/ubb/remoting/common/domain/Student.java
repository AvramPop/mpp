package ro.ubb.remoting.common.domain;

import java.util.Objects;

/**
 * Created by radu.
 */
public class Student extends BaseEntity<Long> {
    private String serialNumber;
    private String name;
    private int group;

    public Student() {
        serialNumber = "";
        name = "";
        group = -1;
        setId(-1L);
    }

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
            + "id="
            + getId()
            + ", serialNumber='"
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
        return getId().equals(student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, name, group);
    }

}
