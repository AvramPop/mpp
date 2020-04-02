package ro.ubb.remoting.server.config;

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

  @Value("${username}")
  private String username;

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

    // TODO use env props (or property files)
    basicDataSource.setUrl(url);
    basicDataSource.setUsername(username);
    basicDataSource.setPassword(password);
    basicDataSource.setInitialSize(2);

    return basicDataSource;
  }
}
