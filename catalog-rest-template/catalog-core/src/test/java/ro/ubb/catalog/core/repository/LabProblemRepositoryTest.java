package ro.ubb.catalog.core.repository;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.ITConfig;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.LabProblemService;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data-lab.xml")
public class LabProblemRepositoryTest {

  @Autowired
  private LabProblemRepository labProblemRepository;

  @Test
  public void findAll() throws Exception {
    List<LabProblem> labProblems = labProblemRepository.findAll();
    assertEquals("there should be four lab problems", 4, labProblems.size());
  }

  @Test
  @Transactional
  public void updateLabProblem() throws Exception {
    LabProblem labProblem = new LabProblem(100, "aaa");
    labProblem.setId(1l);
    LabProblem update = labProblemRepository.findById(1L).get();
    update.setProblemNumber(labProblem.getProblemNumber());
    update.setDescription(labProblem.getDescription());
    LabProblem labProblemDB = labProblemRepository.findById(1l).get();
    assertEquals(labProblemDB.getDescription(), labProblem.getDescription());
  }

  @Test
  public void createLabProblem() throws Exception {
    LabProblem labProblem = new LabProblem(5, "descr");
    labProblem.setId(5l);
    labProblemRepository.save(labProblem);
    List<LabProblem> labs = labProblemRepository.findAll();
    assertEquals("there should be five lab problems", 5, labs.size());

  }

  @Test
  public void deleteLabProblem() throws Exception {
    LabProblem labProblem = new LabProblem(111, "sn1");
    labProblem.setId(1l);
    labProblemRepository.delete(labProblem);
    List<LabProblem> labs = labProblemRepository.findAll();
    assertEquals("there should be five students", 3, labs.size());
  }

}