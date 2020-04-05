package ro.ubb.remoting.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ro.ubb.remoting.common.service.LabProblemService;

@Configuration
public class LabProblemClientConfig {
  @Bean
  RmiProxyFactoryBean rmiLabProblemProxyFactoryBean() {
    RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
    rmiProxyFactoryBean.setServiceInterface(LabProblemService.class);
    rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/LabProblemService");
    return rmiProxyFactoryBean;
  }
}
