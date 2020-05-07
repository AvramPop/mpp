package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    log.trace("getAssignments call - params");
    return new AssignmentsDto(
        assignmentConverter.convertModelsToDtos(assignmentService.getAllAssignments()));
  }

  @RequestMapping(value = "/assignments/page/{pageNumber}/{perPage}", method = RequestMethod.GET)
  PagedAssignmentsDto getAssignments(@PathVariable Integer pageNumber, @PathVariable Integer perPage) {
    log.trace("getAssignments call - params:");
    return conversionFactory.convertPagedAssignmentsToDtos(assignmentService.getAllAssignments(pageNumber, perPage));
  }

  @RequestMapping(value = "/assignments", method = RequestMethod.POST)
  ResponseDto saveAssignment(@RequestBody AssignmentDto assignmentDto) {
    log.trace("saveAssignment call - params = assignmentDto:{}", assignmentDto);
    try {
      if (assignmentService.saveAssignment(assignmentConverter.convertDtoToModel(assignmentDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.PUT)
  ResponseDto updateAssignment(@PathVariable Long id, @RequestBody AssignmentDto assignmentDto) {
    log.trace("updateAssignment call - params = assignmentDto:{}", assignmentDto);
    try {
      if (assignmentService.updateAssignment(
          id, assignmentConverter.convertDtoToModel(assignmentDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/assignments/sorted", method = RequestMethod.POST)
  AssignmentsDto getAssignmentsSorted(@RequestBody SortDto sort) {
    log.trace("getAssignmentsSorted call - params = sort:{}", sort);
    return new AssignmentsDto(
        assignmentConverter.convertModelsToDtos(
            assignmentService.getAllAssignmentsSorted(sortConverter.convertDtoToSort(sort))));
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.DELETE)
  ResponseDto deleteAssignment(@PathVariable Long id) {
    // todo:log
    log.trace("deleteAssignment call - params = id:{}", id);
    try {
      if (assignmentService.deleteAssignment(id)) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/assignments/{id}", method = RequestMethod.GET)
  AssignmentDto getAssignment(@PathVariable Long id) {
    log.trace("getAssignment call - params = id:{}", id);
    return assignmentConverter.convertModelToDto(assignmentService.getAssignmentById(id));
  }

  @RequestMapping(value = "/logic/mean", method = RequestMethod.GET)
  PairDto<Long, Double> getGreatestMean() {
    log.trace("getGreatestMean call - params");
    return conversionFactory.convertMeanToDto(assignmentService.greatestMean());
  }

  @RequestMapping(value = "/logic/assigned", method = RequestMethod.GET)
  PairDto<Long, Long> getIdOfLabProblemMostAssigned() {
    log.trace("getIdOfLabProblemMostAssigned call - params");
    return conversionFactory.convertIdToDto(assignmentService.idOfLabProblemMostAssigned());
  }

  @RequestMapping(value = "/logic/avg", method = RequestMethod.GET)
  DoubleDto getAverageGrade() {
    log.trace("getAverageGrade call - params");
    return conversionFactory.convertDoubleToDto(assignmentService.averageGrade());
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.DELETE)
  ResponseDto deleteLabProblem(@PathVariable Long id) {
    log.trace("deleteLabProblem call - params = id:{}", id);
    try {
      if (assignmentService.deleteLabProblem(id)) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
  ResponseDto deleteStudent(@PathVariable Long id) {
    log.trace("deleteStudent call - params = id:{}", id);
    try {
      if (assignmentService.deleteStudent(id)) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }
}
