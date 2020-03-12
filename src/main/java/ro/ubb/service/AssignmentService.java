package ro.ubb.service;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.LabProblem;
import ro.ubb.domain.exceptions.RepositoryException;
import ro.ubb.domain.exceptions.ValidatorException;
import ro.ubb.domain.validators.Validator;
import ro.ubb.repository.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AssignmentService {

    private Repository<Long, Assignment> repository;
    private LabProblemService labProblemService;
    private StudentService studentService;
    private Validator<Assignment> assignmentValidator;
    public AssignmentService(Repository<Long, Assignment> repository, LabProblemService labProblemService, StudentService studentService, Validator<Assignment> assignmentValidator) {
        this.repository = repository;
        this.studentService = studentService;
        this.labProblemService = labProblemService;
        this.assignmentValidator = assignmentValidator;
    }

    public Optional<Assignment> addAssignment(Long id, Long studentID, Long labProblemID) throws ValidatorException {

        if (studentService.getStudentById(studentID).isPresent()
        && labProblemService.getLabProblemById(labProblemID).isPresent()) {
          Assignment assignment = new Assignment(studentID, labProblemID);
          assignment.setId(id);
          assignmentValidator.validate(assignment);
          return repository.save(assignment);
        }
        throw new RepositoryException("Invalid assignment");
    }

    public Set<Assignment> getAllAssignments() {
        Iterable<Assignment> problems = repository.findAll();
        return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
    }

}
