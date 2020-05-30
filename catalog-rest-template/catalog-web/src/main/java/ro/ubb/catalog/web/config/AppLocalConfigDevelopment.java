package ro.ubb.catalog.web.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import ro.ubb.catalog.core.config.JPAConfig;
@Configuration
@ComponentScan({"ro.ubb.catalog.core"})
@Import({JPAConfig.class})
@PropertySources({
  @PropertySource(value = "classpath:local/db-dev.properties"),
})
@Profile("dev")
public class AppLocalConfigDevelopment implements AppLocalConfigInterface{

}
