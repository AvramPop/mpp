package ro.ubb.remoting.server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.server.repository.StudentRepository;
import ro.ubb.remoting.server.service.StudentServiceImpl;
import ro.ubb.remoting.server.service.validators.StudentValidator;

@Configuration
public class StudentServerConfig implements ApplicationContextAware {
  private static ApplicationContext context;

  public ApplicationContext getApplicationContext() {
    return context;
  }

  @Bean
  RmiServiceExporter rmiStudentServiceExporter() {
    RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
    rmiServiceExporter.setServiceName("StudentService");
    rmiServiceExporter.setServiceInterface(StudentService.class);
    rmiServiceExporter.setService(studentService());
    return rmiServiceExporter;
  }

  @Bean
  StudentService studentService() {

    StudentRepository studentRepository = (StudentRepository) context.getBean("studentRepository");
    return new StudentServiceImpl(studentRepository, new StudentValidator());
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
}
