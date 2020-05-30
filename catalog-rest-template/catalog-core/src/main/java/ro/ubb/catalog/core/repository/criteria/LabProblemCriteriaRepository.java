package ro.ubb.catalog.core.repository.criteria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.LabProblemCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
@Component("LabProblemCriteriaRepositoryImpl")
//@Profile("criteria")
public class LabProblemCriteriaRepository implements LabProblemCustomRepository {
  public static final Logger log = LoggerFactory.getLogger(LabProblemCriteriaRepository.class);

  @PersistenceContext
  private EntityManager entityManager;
  public List<LabProblem> findByProblemNumberCustom(int problemNumber) {
    log.trace("Lab Criteria: find by number");
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    CriteriaQuery<LabProblem> criteriaQuery = criteriaBuilder.createQuery(LabProblem.class);
    criteriaQuery.distinct(Boolean.TRUE);
    Root<LabProblem> labProblemRoot = criteriaQuery.from(LabProblem.class);
    ParameterExpression<Integer> pe = criteriaBuilder.parameter(Integer.class);
    criteriaQuery.where(criteriaBuilder.equal(labProblemRoot.get("problemNumber"), pe));

    TypedQuery<LabProblem> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(pe, problemNumber);
    return query.getResultList();
  }

  public List<LabProblem> findByDescriptionCustom(String description) {
    log.trace("Lab Criteria: find by description");

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

    CriteriaQuery<LabProblem> criteriaQuery = criteriaBuilder.createQuery(LabProblem.class);
    criteriaQuery.distinct(Boolean.TRUE);
    Root<LabProblem> labProblemRoot = criteriaQuery.from(LabProblem.class);
    ParameterExpression<String> pe = criteriaBuilder.parameter(String.class);
    criteriaQuery.where(criteriaBuilder.equal(labProblemRoot.get("description"), pe));

    TypedQuery<LabProblem> query = entityManager.createQuery(criteriaQuery);
    query.setParameter(pe, description);
    return query.getResultList();
  }
}
