package ro.ubb.socket.client.service;

import ro.ubb.socket.common.domain.Assignment;
import ro.ubb.socket.common.domain.LabProblem;
import ro.ubb.socket.common.domain.Student;
import ro.ubb.socket.common.domain.exceptions.ValidatorException;
import ro.ubb.socket.common.service.AssignmentService;
import ro.ubb.socket.common.service.sort.Sort;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class AssignmentClientService implements AssignmentService {
  @Override
  public Future<Assignment> addAssignment(Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException{
    return null;
  }

  @Override
  public Future<Set<Assignment>> getAllAssignments(){
    return null;
  }

  @Override
  public Future<List<Assignment>> getAllAssignmentsSorted(Sort sort){
    return null;
  }

  @Override
  public Future<Assignment> getAssignmentById(Long id){
    return null;
  }

  @Override
  public Future<Assignment> deleteAssignment(Long id){
    return null;
  }

  @Override
  public Future<Student> deleteStudent(Long id){
    return null;
  }

  @Override
  public Future<LabProblem> deleteLabProblem(Long id){
    return null;
  }

  @Override
  public Future<Assignment> updateAssignment(Long id, Long studentID, Long labProblemID, int grade) throws ValidatorException{
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Double>> greatestMean(){
    return null;
  }

  @Override
  public Future<AbstractMap.SimpleEntry<Long, Long>> idOfLabProblemMostAssigned(){
    return null;
  }

  @Override
  public Future<Double> averageGrade(){
    return null;
  }

  @Override
  public Future<Map<Student, List<LabProblem>>> studentAssignedProblems(){
    return null;
  }
}
