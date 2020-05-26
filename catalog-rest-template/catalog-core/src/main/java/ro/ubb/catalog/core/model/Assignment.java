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
@Table(name="assignment")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class Assignment extends BaseEntity<Long> {
  @NonNull
  private Integer grade;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "studentid")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Student student;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "labproblemid")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private LabProblem labProblem;
}
