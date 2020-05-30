package ro.ubb.catalog.core.model;

import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

/** Created by radu. */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="student")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedEntityGraphs({
    @NamedEntityGraph(name = "allStudentsForAssignments",
        attributeNodes = @NamedAttributeNode(value = "assignments")),
    @NamedEntityGraph(name = "allStudentsForAssignmentsWithLabProblems",
        attributeNodes = @NamedAttributeNode(value = "assignments", subgraph = "assignmentWithLabProblem"),
        subgraphs = @NamedSubgraph(name = "assignmentWithLabProblem",
            attributeNodes = @NamedAttributeNode(value = "labProblem")
        )),
    @NamedEntityGraph(name = "allStudentsWithGroupNumber",
        attributeNodes = @NamedAttributeNode(value = "assignments"))
})
public class Student extends BaseEntity<Long> {
  @NonNull
  @Column(name = "serialnumber")
  private String serialNumber;
  @NonNull
  private String name;
  @NonNull
  @Column(name = "groupnumber")
  private Integer groupNumber;
  @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<Assignment> assignments;
}
