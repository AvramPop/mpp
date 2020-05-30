package ro.ubb.catalog.core.repository;

import ro.ubb.catalog.core.model.Student;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

public interface StudentCustomRepository {
  List<Student> findByGroupNumberCustom(int groupNumber);
  List<Student> findByNameCustom(String name);
}
