package ro.ubb.catalog.core.repository.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.LabProblemCustomRepository;
import ro.ubb.catalog.core.repository.StudentCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
@Component("StudentCriteriaRepositoryImpl")
//@Profile("criteria")
public class StudentCriteriaRepository implements StudentCustomRepository {
  public static final Logger log = LoggerFactory.getLogger(StudentCriteriaRepository.class);

  @PersistenceContext
  private EntityManager entityManager;
  public List<Student> findByGroupNumberCustom(int groupNumber) {
    log.trace("#################");
    log.trace("Student Criteria: find by number");
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
    criteriaQuery.distinct(Boolean.TRUE);
    Root<Student> studentRoot = criteriaQuery.from(Student.class);
    ParameterExpression<Integer> pe = criteriaBuilder.parameter(Integer.class);
    criteriaQuery.where(criteriaBuilder.equal(studentRoot.get("groupNumber"), pe));

    TypedQuery<Student> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(pe, groupNumber);
    return query.getResultList();
  }

  public List<Student> findByNameCustom(String name) {
    log.trace("#################");
    log.trace("Student Criteria: find by name");

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

      CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
      criteriaQuery.distinct(Boolean.TRUE);
      Root<Student> studentRoot = criteriaQuery.from(Student.class);
      ParameterExpression<String> pe = criteriaBuilder.parameter(String.class);
      criteriaQuery.where(criteriaBuilder.equal(studentRoot.get("name"), pe));

      TypedQuery<Student> query = entityManager.createQuery(criteriaQuery);
      query.setParameter(pe, name);
      return query.getResultList();
  }
}
