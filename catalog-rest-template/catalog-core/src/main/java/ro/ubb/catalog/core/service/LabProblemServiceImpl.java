package ro.ubb.catalog.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.repository.LabProblemRepository;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.core.service.validator.AssignmentValidator;
import ro.ubb.catalog.core.service.validator.LabProblemValidator;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.domain.PageRequest.of;

@Service
public class LabProblemServiceImpl implements LabProblemService {
  public static final Logger log = LoggerFactory.getLogger(LabProblemServiceImpl.class);

  @Autowired private LabProblemRepository labProblemRepository;
  @Autowired private LabProblemValidator validator;
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired private AssignmentValidator assignmentValidator;

  @Override
  public List<LabProblem> getAllLabProblems() {
    log.trace("getAllLabProblems --- method entered");

    List<LabProblem> result = labProblemRepository.findAll();

    log.trace("getAllLabProblems: result={}", result);

    return result;
  }

  @Override
  public Page<LabProblem> getAllLabProblems(int pageNumber, int perPage){
    log.trace("getAllLabProblems paginated --- method entered, {}", pageNumber);
    Pageable pageable = of(pageNumber,perPage);
    return labProblemRepository.findAll(pageable);
  }

  @Override
  public List<LabProblem> getAllLabProblemsSorted(Sort sort) {
//    log.trace("getAllLabProblemsSorted - method entered");
//
//    Iterable<LabProblem> labProblems = labProblemRepository.findAll();
//    Iterable<LabProblem> labProblemsSorted = sort.sort(labProblems);
//    log.trace("addAssignment - finished bad");
//    return StreamSupport.stream(labProblemsSorted.spliterator(), false)
//        .collect(Collectors.toList());

    org.springframework.data.domain.Sort springSort = null;
    for(int i = 0; i < sort.getSortingChain().size(); i++){
      org.springframework.data.domain.Sort.Direction direction = sort.getSortingChain().get(i).getKey() == Sort.Direction.ASC ? org.springframework.data.domain.Sort.Direction.ASC : org.springframework.data.domain.Sort.Direction.DESC;
      if(i == 0){
        springSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
      } else {
        org.springframework.data.domain.Sort newSpringSort = new org.springframework.data.domain.Sort(direction, sort.getSortingChain().get(i).getValue());
        springSort = springSort.and(newSpringSort);
      }
    }
    Iterable<LabProblem> labProblems = labProblemRepository.findAll(springSort);
    log.trace("getAllLabProblemsSorted - finished well");
    return StreamSupport.stream(labProblems.spliterator(), false)
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
//    Iterable<LabProblem> labProblems = labProblemRepository.findAll();
//    List<LabProblem> filteredLabProblems = new ArrayList<>();
//    labProblems.forEach(filteredLabProblems::add);
//    filteredLabProblems.removeIf(entity -> entity.getProblemNumber() != problemNumberToFilterBy);
    List<LabProblem> filteredLabProblems = labProblemRepository.findByProblemNumber(problemNumberToFilterBy);
    log.trace("filterByProblemNumber - finished well");
    return filteredLabProblems;
  }

  @Override
  @Transactional
  public boolean saveAssignment(Long id, Long studentId, Long labProblemId, int grade) {
    Assignment assignment =
        new Assignment(
            grade,
            entityManager.getReference(Student.class, studentId),
            entityManager.getReference(LabProblem.class, labProblemId));
    assignment.setId(id);
    assignmentValidator.validate(assignment);

    labProblemRepository.findAllLabProblemsWithAssignments().stream()
        .filter(labProblem -> labProblem.getId().equals(assignment.getLabProblem().getId()))
        .findFirst()
        .get()
        .getAssignments()
        .add(assignment);
    return true;
  }

  public List<Assignment> getAllAssignments() {
    log.trace("getAllAssignments --- method entered");

    List<Assignment> result =
        labProblemRepository.findAllLabProblemsWithAssignments().stream()
            .map(LabProblem::getAssignments)
            .reduce(
                new ArrayList<>(),
                (a, b) -> {
                  a.addAll(b);
                  return a;
                });

    log.trace("getAllAssignments: result={}", result);

    return result;
  }

  @Override
  public AbstractMap.SimpleEntry<Long, Long> idOfLabProblemMostAssigned() {
    Iterable<Assignment> assignmentIterable = getAllAssignments();
    Set<Assignment> assignments =
        StreamSupport.stream(assignmentIterable.spliterator(), false).collect(Collectors.toSet());
    log.trace("idOfLabProblemMostAssigned - method entered");
    return labProblemRepository.findAll().stream()
        .map(
            labProblem ->
                new AbstractMap.SimpleEntry<>(
                    labProblem.getId(),
                    assignments.stream()
                        .filter(
                            assignment ->
                                assignment.getLabProblem().getId().equals(labProblem.getId()))
                        .count()))
        .max(((pair1, pair2) -> (int) (pair1.getValue() - pair2.getValue())))
        .orElse(null);
  }


  @Override
  public List<LabProblem> findByProblemNumberCustom(int problemNumber){
    return labProblemRepository.findByProblemNumberCustom(1);
  }

  @Override
  public List<LabProblem> findByDescriptionCustom(String description){
    return labProblemRepository.findByDescriptionCustom(description);
  }
}
