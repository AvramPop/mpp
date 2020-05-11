package ro.ubb.catalog.core.repository;

import ro.ubb.catalog.core.model.LabProblem;

import java.util.List;

public interface LabProblemRepository extends CatalogRepository<LabProblem, Long> {
  List<LabProblem> findByProblemNumber(int problemNumber);
}
