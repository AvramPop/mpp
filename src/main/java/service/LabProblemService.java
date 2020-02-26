package service;

import domain.LabProblem;
import domain.exceptions.ValidatorException;
import repository.Repository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LabProblemService {

    Repository<Long, LabProblem> repository;
    LabProblemService(Repository<Long,LabProblem> repository){
        this.repository = repository;
    }

    /**
     * Adds a new entity to the repository, if it is correct
     * @param labProblem
     */
    public void addLabProblem(LabProblem labProblem) throws ValidatorException
    {
        repository.save(labProblem);
    }

    public Set<LabProblem> getAllLabProblems() {
        Iterable<LabProblem> problems  = repository.findAll();
        return StreamSupport.stream(problems.spliterator(),false).collect(Collectors.toSet());
    }
}
