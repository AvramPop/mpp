package ro.ubb.catalog.web.config;

import org.springframework.context.annotation.*;
import ro.ubb.catalog.core.config.JPAConfig;

@Configuration
@ComponentScan({"ro.ubb.catalog.core"})
@Import({JPAConfig.class})
@PropertySources({
    @PropertySource(value = "classpath:local/db-test.properties"),
})
@Profile("test")
public class AppLocalConfigTesting implements AppLocalConfigInterface{
}
