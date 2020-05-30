package ro.ubb.catalog.core.repository.jpql;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.StudentCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
@Component("StudentJpqlRepositoryImpl")
public class StudentJpqlRepository implements StudentCustomRepository {
  public static final Logger log = LoggerFactory.getLogger(StudentJpqlRepository.class);

  @PersistenceContext
  private EntityManager entityManager;
  public List<Student> findByGroupNumberCustom(@Param("groupNumberParam") int groupNumberParam) {
    log.trace("Student JPQL: find by number");
    Query query =
        entityManager.createQuery(
            "SELECT DISTINCT a from Student a WHERE a.groupNumber = :groupNumberParam");
    query.setParameter("groupNumberParam", groupNumberParam);
    return query.getResultList();
//
//    Session hibernateEntityManager = entityManager.unwrap(Session.class);
//    Session session = hibernateEntityManager.getSession();
//    org.hibernate.Query query =
//        session
//            .createSQLQuery(
//                "SELECT DISTINCT {s.*} from student s WHERE s.groupNumber=:groupNumberParam")
//            .addEntity("s", Student.class)
//            .setParameter("groupNumberParam", groupNumberParam)
//            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//    return query.getResultList();
  }

  public List<Student> findByNameCustom(@Param("nameParam") String nameParam) {
    log.trace("Student JPQL: find by name");
    Query query =
        entityManager.createQuery(
            "SELECT DISTINCT a from Student a WHERE a.name = :nameParam");
    query.setParameter("nameParam", nameParam);
    return query.getResultList();
//
//    Session hibernateEntityManager = entityManager.unwrap(Session.class);
//      Session session = hibernateEntityManager.getSession();
//      org.hibernate.Query query =
//          session
//              .createSQLQuery(
//                  "SELECT DISTINCT {s.*} from student s WHERE s.name=:nameParam")
//              .addEntity("s", Student.class)
//              .setParameter("nameParam", nameParam)
//              .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//      return query.getResultList();
  }
}
