package ro.ubb.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:app.properties")
public class JDBCConfig {
  @Value("${url}")
  private String url;

  @Value("${user_name}")
  private String user_name;

  @Value("${password}")
  private String password;

  @Bean
  JdbcOperations jdbcOperations() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(dataSource());
    return jdbcTemplate;
  }

  @Bean
  DataSource dataSource() {
    BasicDataSource basicDataSource = new BasicDataSource();
    basicDataSource.setUrl(url);
    basicDataSource.setUsername(user_name);
    basicDataSource.setPassword(password);
    basicDataSource.setInitialSize(2);
    return basicDataSource;
  }
}
