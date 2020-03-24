package ro.ubb.repository.db.sort;

import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.ClassReflectionException;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {
  private Sort sort;
  private List<LabProblem> problems;
  @BeforeEach
  void setUp() {
    sort = new Sort("description");
    problems = new LinkedList<>();
    LabProblem labProblem = new LabProblem(11, "description");
    labProblem.setId(1L);
    problems.add(labProblem);
    labProblem = new LabProblem(22, "descriptionTwo");
    labProblem.setId(2L);
    problems.add(labProblem);

  }

  @AfterEach
  void tearDown() {
    sort = null;
    problems = null;
  }
  @Test
  void Given_NewSortEntity_When_NoElementIsSetInTheParameter_Then_IllegalArgumentExceptionIsThrown() {
    Assertions.assertThrows(IllegalArgumentException.class, Sort::new);
  }

  @Test
  void Given_SortWithNoClassName_When_ClassNameIsNotSet_Then_IllegalStateExceptionIsThrown() {
    Assertions.assertThrows(IllegalStateException.class,()-> sort.sort(problems));
  }
  @Test
  void Given_WithEmptyClassName_When_Sorting_Then_IllegalStateExceptionsThrown() {
    sort.setClassName("");
    Assertions.assertThrows(IllegalStateException.class,()-> sort.sort(problems));
  }
  @Test
  void Given_NewSortEntity_When_InvaidSortingChainIsSet_Then_ThrowsException() {
    sort.setClassName("LabProblem");
    List<Map.Entry<Sort.Direction, String>> sortingChain = new LinkedList<>();
    sortingChain.add(new AbstractMap.SimpleEntry<>(Sort.Direction.ASC,"groupNumber1"));
    sort.setSortingChain(sortingChain);
    Assertions.assertThrows(ClassReflectionException.class,()->sort.sort(problems));
  }

  @Test
  void Given_SortWithClassNameSet_When_Sorting_Then_NoExceptionsIsThrown() {
    sort.setClassName("LabProblem");
    Assertions.assertDoesNotThrow(()->{sort.sort(problems);});
  }
  @Test
  void Given_SortWithClassNameSetAndSortingForDescriptionOnLabProblem_When_ClassNameIsSetAndSortingInAscendingOrder_Then_EntitiesAreSorted() {
    sort.setClassName("LabProblem");
    Assertions.assertEquals(sort.sort(problems),problems.stream().sorted(Comparator.comparing(LabProblem::getDescription)).collect(Collectors.toCollection(ArrayList::new)));
  }

  @Test
  void Given_SortWithClassNameSetAndSortingForDescriptionOnLabProblem_When_ClassNameIsSetAndSortingInDescendingOrder_Then_EntitiesAreSorted() {
    sort = new Sort(Sort.Direction.DESC,"description");
    sort.setClassName("LabProblem");
    Assertions.assertEquals(sort.sort(problems),problems.stream().sorted(Comparator.comparing(LabProblem::getDescription).reversed()).collect(Collectors.toCollection(ArrayList::new)));
  }

  @Test
  void Given_NewSortEntity_When_SortingChainIsSetAndBeingSortedByIt_Then_ItSortingChainItIsSortedCorrectly() {
    List<Map.Entry<Sort.Direction, String>> sortingChain = new LinkedList<>();
    sort.setClassName("LabProblem");

    sortingChain.add(new AbstractMap.SimpleEntry<>(Sort.Direction.DESC,"problemNumber"));
    sort.setSortingChain(sortingChain);
    Assertions.assertEquals(sort.sort(problems),problems.stream().sorted(Comparator.comparing(LabProblem::getProblemNumber).reversed()).collect(Collectors.toCollection(ArrayList::new)));

  }

  @Test
  void Given_NewSortEntityAndAnotherOne_When_SortingChainIsSetAndAnotherSortIsAddedToItBeingSortedByIt_Then_ItSortingChainItIsSortedCorrectly() {
    List<Map.Entry<Sort.Direction, String>> sortingChain = new LinkedList<>();
    sort.setClassName("LabProblem");
    LabProblem labProblem = new LabProblem(11, "descriptionThree");
    labProblem.setId(3L);
    problems.add(labProblem);
    labProblem = new LabProblem(11, "descriptionTwo");
    labProblem.setId(4L);
    problems.add(labProblem);

    sortingChain.add(new AbstractMap.SimpleEntry<>(Sort.Direction.DESC,"problemNumber"));
    sort.setSortingChain(sortingChain);

    sort.and(new Sort("description"));

    Comparator<LabProblem> compareValues = Comparator
            .comparing(LabProblem::getProblemNumber).reversed()
            .thenComparing(LabProblem::getDescription);

    sort.sort(problems).forEach(System.out::println);

    Assertions.assertEquals(sort.sort(problems),problems.stream().sorted(compareValues).collect(Collectors.toCollection(ArrayList::new)));

  }

}