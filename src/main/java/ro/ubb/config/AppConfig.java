package ro.ubb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ro.ubb.UI", "ro.ubb.service", "ro.ubb.repository", "ro.ubb.service.validators"})
public class AppConfig {
  //    @Bean
  //    Console console(){return new Console();}
  //    @Bean
  //    StudentService studentService(){return new StudentServiceImplementation(); }
  //    @Bean
  //    SortingRepository<Long, Student> studentRepository(){return new StudentRepository();}
  //    @Bean
  //    Validator<Student> studentValidator(){return new StudentValidator(); }
  //    @Bean
  //    LabProblemService labProblemService(){return new LabProblemServiceImplementation(); }
  //    @Bean
  //    SortingRepository<Long, LabProblem> labProblemRepository(){return new
  // LabProblemRepository();}
  //    @Bean
  //    Validator<LabProblem> labProblemValidator(){return new LabProblemValidator(); }
  //    @Bean
  //    AssignmentService assignmentService(){return new AssignmentServiceImplementation(); }
  //    @Bean
  //    SortingRepository<Long, Assignment> assignmentRepository(){return new
  // AssignmentRepository();}
  //    @Bean
  //    Validator<Assignment> assignmentValidator(){return new AssignmentValidator(); }

}
