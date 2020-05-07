package ro.ubb.catalog.core.model;

import lombok.*;
import org.hibernate.annotations.Proxy;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;

/** Created by radu. */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@Proxy(lazy = false)
public class Student extends BaseEntity<Long> {
  @NonNull
  private String serialNumber;
  @NonNull
  private String name;
  @NonNull
  private Integer groupNumber;
}
