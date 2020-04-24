package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.AssignmentService;
import ro.ubb.catalog.web.converter.AssignmentConverter;
import ro.ubb.catalog.web.converter.ConversionFactory;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.dto.*;

@RestController
public class AssignmentController {
  public static final Logger log = LoggerFactory.getLogger(AssignmentController.class);

  @Autowired private AssignmentService assignmentService;

  @Autowired private AssignmentConverter assignmentConverter;

  @Autowired private ConversionFactory conversionFactory;

  @Autowired private SortConverter sortConverter;

  @RequestMapping(value = "/assignments", method = RequestMethod.GET)
  AssignmentsDto getAssignments() {
    // todo: log
    return new AssignmentsDto(
        assignmentConverter.convertModelsToDtos(assignmentService.getAllAssignments()));
  }

  @RequestMapping(value = "/assignments", method = RequestMethod.POST)
  ResponseEntity<?> saveAssignment(@RequestBody AssignmentDto assignmentDto) {
    // todo log
    if (assignmentService.saveAssignment(assignmentConverter.convertDtoToModel(assignmentDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.PUT)
  ResponseEntity<?> updateAssignment(
      @PathVariable Long id, @RequestBody AssignmentDto assignmentDto) {
    // todo: log
    if (assignmentService.updateAssignment(
        id, assignmentConverter.convertDtoToModel(assignmentDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/assignments/sorted", method = RequestMethod.POST)
  AssignmentsDto getAssignmentsSorted(@RequestBody SortDto sort) {
    // todo: log
    return new AssignmentsDto(
        assignmentConverter.convertModelsToDtos(
            assignmentService.getAllAssignmentsSorted(sortConverter.convertDtoToSort(sort))));
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.DELETE)
  ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
    // todo:log

    if (assignmentService.deleteAssignment(id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.GET)
  AssignmentDto getAssignment(@PathVariable Long id) {
    // todo: log
    return assignmentConverter.convertModelToDto(assignmentService.getAssignmentById(id));
  }

  @RequestMapping(value = "/logic/mean", method = RequestMethod.GET)
  PairDto<Long, Double> getGreatestMean() {
    // todo: log
    return conversionFactory.convertMeanToDto(assignmentService.greatestMean());
  }

  @RequestMapping(value = "/logic/assigned", method = RequestMethod.GET)
  PairDto<Long, Long> getIdOfLabProblemMostAssigned() {
    // todo: log
    return conversionFactory.convertIdToDto(assignmentService.idOfLabProblemMostAssigned());
  }

  @RequestMapping(value = "/logic/avg", method = RequestMethod.GET)
  DoubleDto getAverageGrade() {
    // todo: log
    return conversionFactory.convertDoubleToDto(assignmentService.averageGrade());
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.DELETE)
  ResponseEntity<?> deleteLabProblem(@PathVariable Long id) {

    if (assignmentService.deleteLabProblem(id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
  ResponseEntity<?> deleteStudent(@PathVariable Long id) {
    // todo:log

    if (assignmentService.deleteStudent(id)) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
