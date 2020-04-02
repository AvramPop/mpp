package ro.ubb.remoting.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ro.ubb.remoting.common.service.AssignmentService;

@Configuration
public class AssignmentClientConfig {
  @Bean
  RmiProxyFactoryBean rmiAssignmentProxyFactoryBean() {
    RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
    rmiProxyFactoryBean.setServiceInterface(AssignmentService.class);
    rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/AssignmentService");
    return rmiProxyFactoryBean;
  }
}
