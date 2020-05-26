package ro.ubb.catalog.core.model;

import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="labproblem")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NamedEntityGraphs({
    @NamedEntityGraph(name = "allLabProblemsWithProblemNumber",
        attributeNodes = @NamedAttributeNode(value = "assignments")),
    @NamedEntityGraph(name = "allLabProblemsForAssignmentsWithStudents",
        attributeNodes = @NamedAttributeNode(value = "assignments", subgraph = "assignmentWithStudent"),
        subgraphs = @NamedSubgraph(name = "assignmentWithStudent",
            attributeNodes = @NamedAttributeNode(value = "student")
        )),
    @NamedEntityGraph(name = "allLabProblemsForAssignments",
        attributeNodes = @NamedAttributeNode(value = "assignments"))
})
public class LabProblem extends BaseEntity<Long> {
  @Column(name = "problemnumber")
  private int problemNumber;
  @NonNull
  private String description;
  @OneToMany(mappedBy = "labProblem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<Assignment> assignments;
}
