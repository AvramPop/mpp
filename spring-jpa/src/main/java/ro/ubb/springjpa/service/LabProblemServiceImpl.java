package ro.ubb.springjpa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.springjpa.exceptions.ValidatorException;
import ro.ubb.springjpa.model.LabProblem;
import ro.ubb.springjpa.repository.LabProblemRepository;
import ro.ubb.springjpa.service.sort.Sort;
import ro.ubb.springjpa.service.validator.LabProblemValidator;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LabProblemServiceImpl implements LabProblemService {
  public static final Logger log = LoggerFactory.getLogger(LabProblemServiceImpl.class);
  @Autowired private LabProblemRepository repository;
  @Autowired private LabProblemValidator validator;

  @Override
  public Optional<LabProblem> addLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem newLabProblem = new LabProblem(problemNumber, description);
    newLabProblem.setId(id);

    if(getLabProblemById(id).isPresent()) return Optional.of(newLabProblem);
    validator.validate(newLabProblem);
    try {
      repository.save(newLabProblem);
      return Optional.empty();
    } catch (JpaSystemException e) {
      return Optional.of(newLabProblem);
    }
  }

  @Override
  public Set<LabProblem> getAllLabProblems() {
    Iterable<LabProblem> problems = repository.findAll();
    return StreamSupport.stream(problems.spliterator(), false).collect(Collectors.toSet());
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    Iterable<LabProblem> labProblems = repository.findAll();
    Iterable<LabProblem> labProblemsSorted = sort.sort(labProblems);
    return StreamSupport.stream(labProblemsSorted.spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<LabProblem> getLabProblemById(Long id) {
    if (id == null || id < 0) {
      throw new IllegalArgumentException("invalid id!");
    }
    return repository.findById(id);
  }

  @Override
  public Optional<LabProblem> deleteLabProblem(Long id) {
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      Optional<LabProblem> entity = repository.findById(id);
      repository.deleteById(id);
      return entity;
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional
  public Optional<LabProblem> updateLabProblem(Long id, int problemNumber, String description)
      throws ValidatorException {
    LabProblem labProblem = new LabProblem(problemNumber, description);
    labProblem.setId(id);
    validator.validate(labProblem);
    if(getLabProblemById(id).isEmpty()) return Optional.of(labProblem);

    try {
      log.trace("updateLabProblem - method entered: labProblem={}", labProblem);
      repository
          .findById(labProblem.getId())
          .ifPresent(
              l -> {
                l.setDescription(labProblem.getDescription());
                l.setProblemNumber(labProblem.getProblemNumber());
                log.debug("updateLabProblem - updated: labProblem={}", l);
              });
      log.trace("updateLabProblem - method finished");
      return Optional.empty();

    } catch (JpaSystemException e) {
      return Optional.of(labProblem);
    }
  }

  @Override
  public Set<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    if (problemNumberToFilterBy < 0) {
      throw new IllegalArgumentException("problem number negative!");
    }
    Iterable<LabProblem> labProblems = repository.findAll();
    Set<LabProblem> filteredLabProblems = new HashSet<>();
    labProblems.forEach(filteredLabProblems::add);
    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    return filteredLabProblems;
  }
}
