package ro.ubb.catalog.core.repository;

import ro.ubb.catalog.core.model.LabProblem;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

public interface LabProblemCustomRepository {
  List<LabProblem> findByProblemNumberCustom(int problemNumber);

  List<LabProblem> findByDescriptionCustom(String description);
}
