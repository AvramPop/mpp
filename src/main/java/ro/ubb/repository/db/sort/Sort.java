package ro.ubb.repository.db.sort;

import ro.ubb.domain.exceptions.ClassReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Sorting provider for iterables using reflection to lookup fields to sort by.
 */
public class Sort {
  private List<Map.Entry<Direction, String>> sortingChain;
  private String className = null;

  public void setClassName(String className) {
    this.className = className;
  }

  public void setSortingChain(List<Map.Entry<Direction, String>> sortingChain){
    this.sortingChain = sortingChain;
  }

  /**
   * Sort given Iterable by the previously given criteria.
   */
  public <T> Iterable<T> sort(Iterable<T> iterableToSort) {
    if (className == null || className.equals("")) throw new IllegalStateException("class name not specified!");
    return StreamSupport.stream(iterableToSort.spliterator(), false)
        .sorted(new SortComparator())
        .collect(Collectors.toList());
  }

  public Sort(Direction direction, String... fieldsToSortBy) {
    sortingChain = new ArrayList<>();
    if (fieldsToSortBy.length == 0) throw new IllegalArgumentException();

    Stream.of(fieldsToSortBy).forEach(field ->sortingChain.add(new AbstractMap.SimpleEntry<>(direction, field)));

  }

  public Sort(String... fieldsToSortBy) {
    this(Direction.ASC, fieldsToSortBy);
  }

  /**
   * Obtain the value of field named fieldName of object objectToInvokeOn
   */
  private Object getValueByFieldName(Object objectToInvokeOn, String fieldName)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
          IllegalAccessException {
    String fullClassName;
    if (fieldName.equals("id")) {
      fullClassName = getClassWithPackages("BaseEntity");
    } else {
      fullClassName = getClassWithPackages(className);
    }
    Class<?> c = Class.forName(fullClassName);
    Method method = c.getDeclaredMethod(getterOfField(fieldName));
    return method.invoke(objectToInvokeOn);
  }

  private String getClassWithPackages(String className) {
    return "ro.ubb.domain." + className;
  }

  /**
   * Inner class to provide sorting logic for the criteria of current instance.
   */
  private class SortComparator implements Comparator<Object> {
    /**
     * Create compare function chaining class criteria for sorting.
     */
    @Override
    public int compare(Object first, Object second) {
      return sortingChain.stream()
          .map(sortingCriteria -> compareObjectsByGivenCriteria(first, second, sortingCriteria))
          .filter(value -> value != 0)
          .findFirst()
          .orElse(0);
    }

    /**
     * Compare given objects by given criteria.
     */
    private int compareObjectsByGivenCriteria(
        Object first, Object second, Map.Entry<Direction, String> sortingCriteria) {
      int result;
      try {
        Object firstValue = getValueByFieldName(first, sortingCriteria.getValue());
        Object secondValue = getValueByFieldName(second, sortingCriteria.getValue());
        result = ((Comparable) firstValue).compareTo(secondValue);
        if (sortingCriteria.getKey() == Direction.DESC) {
          result *= -1; // change ascending to descending
        }
      } catch (ClassNotFoundException
          | NoSuchMethodException
          | InvocationTargetException
          | IllegalAccessException e) {
        throw new ClassReflectionException("no such class or field!");
      }
      return result;
    }
  }

  /**
   * Chain sorting criteria.
   */
  public Sort and(Sort sort) {
    this.sortingChain.addAll(sort.sortingChain);
    return this;
  }

  private String getterOfField(String fieldName) {
    String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    return "get" + fieldNameCapitalized;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("Sort{" + "sortingChain=\n");
    for (Map.Entry<Direction, String> entry : sortingChain) {
      result.append(entry.toString());
      result.append("\n");
    }
    result.append("}");
    return result.toString();
  }

  /**
   * Sorting directions for a given field.
   */
  public enum Direction {
    ASC,
    DESC
  }
}
