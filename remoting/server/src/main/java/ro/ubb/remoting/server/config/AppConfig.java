package ro.ubb.remoting.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.domain.LabProblem;
import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.server.repository.AssignmentRepository;
import ro.ubb.remoting.server.repository.LabProblemRepository;
import ro.ubb.remoting.server.repository.SortingRepository;
import ro.ubb.remoting.server.repository.StudentRepository;

@Configuration
public class AppConfig {
  @Bean
  SortingRepository<Long, Student> studentRepository() {
    return new StudentRepository();
  }
  @Bean
  SortingRepository<Long, LabProblem> labProblemRepository() {
    return new LabProblemRepository();
  }
  @Bean
  SortingRepository<Long, Assignment> assignmentRepository() {
    return new AssignmentRepository();
  }
}
