package ro.ubb.catalog.core.repository;

import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;

import java.util.List;

/** Created by radu. */
public interface StudentRepository extends CatalogRepository<Student, Long> {
  List<Student> findByGroupNumber(int groupNumber);

}
