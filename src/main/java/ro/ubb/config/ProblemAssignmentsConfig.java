package ro.ubb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"ro.ubb.UI", "ro.ubb.service", "ro.ubb.repository", "ro.ubb.service.validators"})
public class ProblemAssignmentsConfig {}
