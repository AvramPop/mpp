package ro.ubb.catalog.core.repository.jpql;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.repository.LabProblemCustomRepository;
import ro.ubb.catalog.core.repository.criteria.StudentCriteriaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Component("LabProblemJpqlRepositoryImpl")
public class LabProblemJpqlRepository implements LabProblemCustomRepository {
  @PersistenceContext private EntityManager entityManager;
  public static final Logger log = LoggerFactory.getLogger(LabProblemJpqlRepository.class);

  public List<LabProblem> findByProblemNumberCustom(@Param("problemNumberParam") int problemNumberParam)  {
    log.trace("Lab JPQL: find by number");
    Query query =
        entityManager.createQuery(
            "SELECT DISTINCT a from LabProblem a WHERE a.problemNumber = :problemNumberParam");
    query.setParameter("problemNumberParam", problemNumberParam);
    return query.getResultList();
//
//    Session hibernateEntityManager = entityManager.unwrap(Session.class);
//    Session session = hibernateEntityManager.getSession();
//    org.hibernate.Query query =
//        session
//            .createSQLQuery(
//                "SELECT DISTINCT {l.*} from labproblem l WHERE l.problemnumber=:problemNumberParam")
//            .addEntity("l", LabProblem.class)
//            .setParameter("problemNumberParam", problemNumberParam)
//            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//    return query.getResultList();
  }

  public List<LabProblem> findByDescriptionCustom(@Param("descriptionParam") String descriptionParam) {
    log.trace("Lab JPQL: find by description");
    Query query =
        entityManager.createQuery(
            "SELECT DISTINCT a from LabProblem a WHERE a.description = :descriptionParam");
    query.setParameter("descriptionParam", descriptionParam);
    return query.getResultList();
//
//    Session hibernateEntityManager = entityManager.unwrap(Session.class);
//    Session session = hibernateEntityManager.getSession();
//    org.hibernate.Query query =
//        session
//            .createSQLQuery(
//                "SELECT DISTINCT {l.*} from labproblem l WHERE l.description=:descriptionParam")
//            .addEntity("l", LabProblem.class)
//            .setParameter("descriptionParam", descriptionParam)
//            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//    return query.getResultList();
  }
}
