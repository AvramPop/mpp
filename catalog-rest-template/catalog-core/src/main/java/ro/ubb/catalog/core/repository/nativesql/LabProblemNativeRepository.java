package ro.ubb.catalog.core.repository.nativesql;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.LabProblemCustomRepository;
import ro.ubb.catalog.core.repository.jpql.StudentJpqlRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Component("LabProblemNativeRepositoryImpl")
public class LabProblemNativeRepository implements LabProblemCustomRepository {
  public static final Logger log = LoggerFactory.getLogger(LabProblemNativeRepository.class);

  @PersistenceContext
  private EntityManager entityManager;
  @Transactional
  public List<LabProblem> findByProblemNumberCustom(@Param("problemNumberParameter") int problemNumberParameter) {
    log.trace("Lab Native: find by number");
    Session hibernateEntityManager = entityManager.unwrap(Session.class);
    Session session = hibernateEntityManager.getSession();
    org.hibernate.Query query =
        session
            .createSQLQuery(
                "SELECT DISTINCT {l.*} from labproblem l WHERE l.problemNumber=:problemNumberParameter")
            .addEntity("l", LabProblem.class)
            .setParameter("problemNumberParameter", problemNumberParameter)
            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    return query.getResultList();
  }
  @Transactional
  public List<LabProblem> findByDescriptionCustom(@Param("descriptionParam") String descriptionParam) {
    log.trace("Lab Native: find by description");
    Session hibernateEntityManager = entityManager.unwrap(Session.class);
    Session session = hibernateEntityManager.getSession();
    org.hibernate.Query query =
        session
            .createSQLQuery(
                "SELECT DISTINCT {l.*} from labproblem l WHERE l.description=:descriptionParam")
            .addEntity("l", LabProblem.class)
            .setParameter("descriptionParam", descriptionParam)
            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    return query.getResultList();
  }
}
