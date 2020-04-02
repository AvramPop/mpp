package ro.ubb.remoting.server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import ro.ubb.remoting.common.service.LabProblemService;
import ro.ubb.remoting.server.repository.LabProblemRepository;
import ro.ubb.remoting.server.service.LabProblemServiceImpl;
import ro.ubb.remoting.server.service.validators.LabProblemValidator;

@Configuration
public class LabProblemServerConfig implements ApplicationContextAware {
  private static ApplicationContext context;

  public ApplicationContext getApplicationContext() {
    return context;
  }

  @Bean
  RmiServiceExporter rmiLabProblemServiceExporter() {
    RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
    rmiServiceExporter.setServiceName("LabProblemService");
    rmiServiceExporter.setServiceInterface(LabProblemService.class);
    rmiServiceExporter.setService(labProblemService());
    return rmiServiceExporter;
  }

  @Bean
  LabProblemService labProblemService() {
    LabProblemRepository labProblemRepository =
        (LabProblemRepository) context.getBean("labProblemRepository");
    return new LabProblemServiceImpl(labProblemRepository, new LabProblemValidator());
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }
}
