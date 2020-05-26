package ro.ubb.catalog.core.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Student;

import java.util.List;
import java.util.Optional;

@Component("StudentCriteriaRepository")
public interface StudentRepository extends CatalogRepository<Student, Long>, StudentCustomRepository {
  @Query("select distinct s from Student s where s.groupNumber = :groupNumber")
  @EntityGraph(value = "allStudentsWithGroupNumber", type =
      EntityGraph.EntityGraphType.LOAD)
  List<Student> findByGroupNumber(@Param("groupNumber") int groupNumber);
  @Query("select distinct s from Student s")
  @EntityGraph(value = "allStudentsForAssignmentsWithLabProblems", type =
      EntityGraph.EntityGraphType.LOAD)
  List<Student> findAllWithAssignments();

  @Query("select distinct s from Student s where s.id=:studentId")
  @EntityGraph(value = "allStudentsForAssignmentsWithLabProblems", type =
      EntityGraph.EntityGraphType.LOAD)
  Optional<Student> findByIdWithAssignments(@Param("studentId") Long studentId);


}
