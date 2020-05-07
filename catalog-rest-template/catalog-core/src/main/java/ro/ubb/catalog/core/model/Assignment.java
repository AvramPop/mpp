package ro.ubb.catalog.core.model;

import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.lang.NonNull;


import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Proxy(lazy = false)
public class Assignment extends BaseEntity<Long> {
  @NonNull
  private Long studentId;
  @NonNull
  private Long labProblemId;
  @NonNull
  private Integer grade;
}
