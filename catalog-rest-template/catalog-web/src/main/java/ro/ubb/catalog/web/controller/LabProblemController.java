package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.LabProblemService;
import ro.ubb.catalog.web.converter.LabProblemConverter;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.dto.LabProblemDto;
import ro.ubb.catalog.web.dto.LabProblemsDto;
import ro.ubb.catalog.web.dto.SortDto;

@RestController
public class LabProblemController {
  public static final Logger log = LoggerFactory.getLogger(LabProblemController.class);

  @Autowired private LabProblemService labProblemService;

  @Autowired private LabProblemConverter labProblemConverter;

  @Autowired private SortConverter sortConverter;

  @RequestMapping(value = "/labs", method = RequestMethod.GET)
  LabProblemsDto getLabProblems() {
    // todo: log
    return new LabProblemsDto(
        labProblemConverter.convertModelsToDtos(labProblemService.getAllLabProblems()));
  }

  @RequestMapping(value = "/labs/bynumber/{problemNumber}", method = RequestMethod.GET)
  LabProblemsDto getLabProblemsFilteredByNumber(@PathVariable Integer problemNumber) {
    // todo: log
    return new LabProblemsDto(
        labProblemConverter.convertModelsToDtos(
            labProblemService.filterByProblemNumber(problemNumber)));
  }

  @RequestMapping(value = "/labs", method = RequestMethod.POST)
  ResponseEntity<?> saveLabProblem(@RequestBody LabProblemDto labProblemDto) {
    // todo log
    if (labProblemService.saveLabProblem(labProblemConverter.convertDtoToModel(labProblemDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/labs/sorted", method = RequestMethod.GET)
  LabProblemsDto getLabProblemsSorted(@RequestBody SortDto sort) {
    // todo: log
    return new LabProblemsDto(
        labProblemConverter.convertModelsToDtos(
            labProblemService.getAllLabProblemsSorted(sortConverter.convertDtoToSort(sort))));
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.PUT)
  ResponseEntity<?> updateLabProblem(
      @PathVariable Long id, @RequestBody LabProblemDto labProblemDto) {
    // todo: log
    if (labProblemService.updateLabProblem(
        id, labProblemConverter.convertDtoToModel(labProblemDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.GET)
  LabProblemDto getLabProblem(@PathVariable Long id) {
    // todo: log
    return labProblemConverter.convertModelToDto(labProblemService.getLabProblem(id));
  }
}
