package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.repository.LabProblemRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.AssignmentValidator;
import ro.ubb.catalog.core.service.validator.LabProblemValidator;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LabProblemServiceImpl implements LabProblemService {
  public static final Logger log = LoggerFactory.getLogger(LabProblemServiceImpl.class);

  @Autowired private LabProblemRepository labProblemRepository;
  @Autowired private LabProblemValidator validator;

  @Override
  public List<LabProblem> getAllLabProblems() {
    log.trace("getAllLabProblems --- method entered");

    List<LabProblem> result = labProblemRepository.findAll();

    log.trace("getAllLabProblems: result={}", result);

    return result;
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
    log.trace("getAllLabProblemsSorted - method entered");

    Iterable<LabProblem> labProblems = labProblemRepository.findAll();
    Iterable<LabProblem> labProblemsSorted = sort.sort(labProblems);
    log.trace("addAssignment - finished bad");
    return StreamSupport.stream(labProblemsSorted.spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public boolean saveLabProblem(LabProblem labProblem) {
    log.trace("saveLabProblem - method entered");
    validator.validate(labProblem);

    if (labProblemRepository.existsById(labProblem.getId())) return false;
    labProblemRepository.save(labProblem);
    return true;
  }

  @Override
  @Transactional
  public boolean updateLabProblem(Long id, LabProblem labProblem) {
    log.trace("updateLabProblem - method entered");
    if (!labProblemRepository.existsById(id)) return false;
    validator.validate(labProblem);

    LabProblem update = labProblemRepository.findById(id).get();
    update.setDescription(labProblem.getDescription());
    update.setProblemNumber(labProblem.getProblemNumber());

    return true;
  }

  @Override
  public boolean deleteLabProblem(Long id) {
    // todo log
    if (id == null || id < 0) throw new IllegalArgumentException("Invalid id!");
    try {
      log.trace("deleteLabProblem - method entered: id={}", id);
      log.debug("deleteLabProblem - updated: a={}", id);
      Optional<LabProblem> entity = labProblemRepository.findById(id);
      if (entity.isEmpty()) return false;

      labProblemRepository.deleteById(id);
      log.trace("addAssignment - finished well");
      return true;
    } catch (EmptyResultDataAccessException e) {
      log.trace("addAssignment - finished bad");
      return false;
    }
  }

  @Override
  public LabProblem getLabProblem(Long id) {
    try {
      return labProblemRepository.getOne(id);
    } catch (EntityNotFoundException e) {
      return null;
    }
  }

  @Override
  public List<LabProblem> filterByProblemNumber(Integer problemNumberToFilterBy) {
    if (problemNumberToFilterBy < 0) {
      throw new IllegalArgumentException("problem number negative!");
    }
    log.trace("filterByProblemNumber - method entered");
    Iterable<LabProblem> labProblems = labProblemRepository.findAll();
    List<LabProblem> filteredLabProblems = new ArrayList<>();
    labProblems.forEach(filteredLabProblems::add);
    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    log.trace("addAssignment - finished well");
    return filteredLabProblems;
  }
}
