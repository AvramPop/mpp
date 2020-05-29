package ro.ubb.catalog.core.service;

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
import ro.ubb.catalog.core.ITConfig;
import ro.ubb.catalog.core.model.LabProblem;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data-lab.xml")
public class LabProblemServiceTest {

  @Autowired
  private LabProblemService labProblemService;

  @Test
  public void findAll() throws Exception {
    List<LabProblem> labProblems = labProblemService.getAllLabProblems();
    assertEquals("there should be four lab problems", 4, labProblems.size());
  }

  @Test
  public void updateLabProblem() throws Exception {
    LabProblem labProblem = new LabProblem(5, "desc");
    labProblem.setId(1l);
    labProblemService.updateLabProblem(1l, labProblem);
    LabProblem problem = labProblemService.getLabProblem(1l);
    assertEquals(problem.getDescription(), labProblem.getDescription());
  }

  @Test
  public void createLabProblem() throws Exception {
    LabProblem labProblem = new LabProblem(5, "descr");
    labProblem.setId(5l);
    labProblemService.saveLabProblem(labProblem);
    List<LabProblem> labs = labProblemService.getAllLabProblems();
    assertEquals("there should be five lab problems", 5, labs.size());

  }

  @Test
  public void deleteLabProblem() throws Exception {
    labProblemService.deleteLabProblem(1L);
    List<LabProblem> labs = labProblemService.getAllLabProblems();
    assertEquals("there should be five students", 3, labs.size());
  }

}