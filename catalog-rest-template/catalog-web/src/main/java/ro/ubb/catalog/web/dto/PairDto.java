package ro.ubb.catalog.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode()
@ToString()
@Builder
public class PairDto<K, V> {
  private K key;
  private V value;
}
