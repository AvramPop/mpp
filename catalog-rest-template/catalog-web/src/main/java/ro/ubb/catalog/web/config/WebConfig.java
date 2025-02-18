package ro.ubb.catalog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Created by radu. */
@Configuration
@EnableWebMvc
@ComponentScan({"ro.ubb.catalog.web.controller", "ro.ubb.catalog.web.converter"})
public class WebConfig {
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:4200", "http://localhost:8082")
            .allowedMethods("GET", "PUT", "POST", "DELETE")
            .exposedHeaders("Access-Control-Allow-Origin:*")
            .exposedHeaders("Access-Control-Allow-Headers:*")
            .exposedHeaders("Access-Control-Allow-Methods:*");
      }
    };
  }
}
