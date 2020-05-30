package ro.ubb.catalog.web.config;


import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ro.ubb.catalog.core.config.JPAConfig;

public interface AppLocalConfigInterface {

  /**
   * Enables placeholders usage with SpEL expressions.
   *
   * @return
   */
  @Bean
  static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
