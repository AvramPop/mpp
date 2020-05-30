package ro.ubb.catalog.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;

import java.util.List;
@Component("LabProblemCriteriaRepository")
public interface LabProblemRepository extends CatalogRepository<LabProblem, Long>, LabProblemCustomRepository {
  @Query("select distinct lp from LabProblem lp where lp.problemNumber = :problemNumber")
  @EntityGraph(value = "allLabProblemsWithProblemNumber", type =
      EntityGraph.EntityGraphType.LOAD)
  List<LabProblem> findByProblemNumber(@Param("problemNumber") int problemNumber);
  @Query("select distinct lp from LabProblem lp")
  @EntityGraph(value = "allLabProblemsForAssignmentsWithStudents", type =
      EntityGraph.EntityGraphType.LOAD)
  List<LabProblem> findAllLabProblemsWithAssignments();
}
