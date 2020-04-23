package ro.ubb.catalog.core.model;

import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Proxy(lazy = false)
public class LabProblem extends BaseEntity<Long> {
  private int problemNumber;
  private String description;
}
