package ro.ubb.remoting.server.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ro.ubb.remoting.common.service.AssignmentService;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.server.repository.AssignmentRepository;
import ro.ubb.remoting.server.repository.LabProblemRepository;
import ro.ubb.remoting.server.repository.StudentRepository;
import ro.ubb.remoting.server.service.AssignmentServiceImpl;
import ro.ubb.remoting.server.service.LabProblemServiceImpl;
import ro.ubb.remoting.server.service.StudentServiceImpl;
import ro.ubb.remoting.server.service.validators.AssignmentValidator;

@Configuration
public class AssignmentServerConfig implements ApplicationContextAware {
  private static ApplicationContext context;

  public static ApplicationContext getApplicationContext() {
    return context;
  }

  @Bean
  RmiServiceExporter rmiAssignmentServiceExporter() {
    RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
    rmiServiceExporter.setServiceName("AssignmentService");
    rmiServiceExporter.setServiceInterface(AssignmentService.class);
    rmiServiceExporter.setService(assignmentService());
    return rmiServiceExporter;
  }

  @Bean
  AssignmentService assignmentService() {
    AssignmentRepository assignmentRepository =
        (AssignmentRepository) context.getBean("assignmentRepository");
    StudentService studentService = (StudentService) context.getBean("studentService");
    LabProblemService labProblemService = (LabProblemService) context.getBean("labProblemService");

    return new AssignmentServiceImpl(
        assignmentRepository, labProblemService, studentService, new AssignmentValidator());
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
}
