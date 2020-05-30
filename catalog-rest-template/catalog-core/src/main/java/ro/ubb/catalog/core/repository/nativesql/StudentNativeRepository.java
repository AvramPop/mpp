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
import ro.ubb.catalog.core.repository.StudentCustomRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Component("StudentNativeRepositoryImpl")
public class StudentNativeRepository implements StudentCustomRepository {
  public static final Logger log = LoggerFactory.getLogger(StudentNativeRepository.class);

  @PersistenceContext
  private EntityManager entityManager;
  @Transactional
  public List<Student> findByGroupNumberCustom(@Param("groupNumberParam") int groupNumberParam) {
    log.trace("Student Native: find by number");
    Session hibernateEntityManager = entityManager.unwrap(Session.class);
    Session session = hibernateEntityManager.getSession();
    org.hibernate.Query query =
        session
            .createSQLQuery(
                "SELECT DISTINCT {s.*} from student s WHERE s.groupNumber=:groupNumberParam")
            .addEntity("s", Student.class)
            .setParameter("groupNumberParam", groupNumberParam)
            .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    return query.getResultList();
  }
  @Transactional
  public List<Student> findByNameCustom(@Param("nameParam") String nameParam) {
    log.trace("Student Native: find by name");

    Session hibernateEntityManager = entityManager.unwrap(Session.class);
      Session session = hibernateEntityManager.getSession();
      org.hibernate.Query query =
          session
              .createSQLQuery(
                  "SELECT DISTINCT {s.*} from student s WHERE s.name=:nameParam")
              .addEntity("s", Student.class)
              .setParameter("nameParam", nameParam)
              .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
      return query.getResultList();
  }
}
