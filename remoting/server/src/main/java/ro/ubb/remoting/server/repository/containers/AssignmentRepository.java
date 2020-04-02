package ro.ubb.remoting.server.repository.containers;

import ro.ubb.remoting.common.domain.Assignment;
import ro.ubb.remoting.common.service.sort.Sort;
import sun.security.validator.ValidatorException;

import java.util.Optional;

public class AssignmentRepository implements SortingRepository<Long, Assignment>{
  @Override
  public Iterable<Assignment> findAll(Sort sort){
    return null;
  }

  @Override
  public Optional<Assignment> findOne(Long aLong){
    return Optional.empty();
  }

  @Override
  public Iterable<Assignment> findAll(){
    return null;
  }

  @Override
  public Optional<Assignment> save(Assignment entity) throws ValidatorException{
    return Optional.empty();
  }

  @Override
  public Optional<Assignment> delete(Long aLong){
    return Optional.empty();
  }

  @Override
  public Optional<Assignment> update(Assignment entity) throws ValidatorException{
    return Optional.empty();
  }
}
