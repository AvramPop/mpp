package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.service.LabProblemService;
import ro.ubb.catalog.core.service.sort.Sort;
import ro.ubb.catalog.web.converter.ConversionFactory;
import ro.ubb.catalog.web.converter.LabProblemConverter;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.dto.*;

import java.util.List;

@RestController
public class LabProblemController {
  public static final Logger log = LoggerFactory.getLogger(LabProblemController.class);

  @Autowired private LabProblemService labProblemService;
  @Autowired private ConversionFactory conversionFactory;

  @Autowired private LabProblemConverter labProblemConverter;

  @Autowired private SortConverter sortConverter;

  @RequestMapping(value = "/labs", method = RequestMethod.GET)
  LabProblemsDto getLabProblems() {
    log.trace("getLabProblems call - params");
    return new LabProblemsDto(
        labProblemConverter.convertModelsToDtos(labProblemService.getAllLabProblems()));
  }

  @RequestMapping(value = "/labs/page/{pageNumber}/{perPage}", method = RequestMethod.GET)
  PagedLabProblemsDto getLabProblems(@PathVariable Integer pageNumber, @PathVariable Integer perPage) {
    log.trace("getLabProblems call - params:");
    return conversionFactory.convertPagedLabProblemsToDtos(labProblemService.getAllLabProblems(pageNumber, perPage));
  }

  @RequestMapping(value = "/labs/bynumber/{problemNumber}", method = RequestMethod.GET)
  LabProblemsDto getLabProblemsFilteredByNumber(@PathVariable Integer problemNumber) {
    log.trace("getLabProblemsFilteredByNumber call - params = problemNumber:{}", problemNumber);
    return new LabProblemsDto(
        labProblemConverter.convertModelsToDtos(
            labProblemService.filterByProblemNumber(problemNumber)));
  }

  @RequestMapping(value = "/labs", method = RequestMethod.POST)
  ResponseDto saveLabProblem(@RequestBody LabProblemDto labProblemDto) {
    log.trace("saveLabProblem call - params = LabProblemDto:{}", labProblemDto);
    try {
      if (labProblemService.saveLabProblem(labProblemConverter.convertDtoToModel(labProblemDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/labs/sorted", method = RequestMethod.POST)
  LabProblemsDto getLabProblemsSorted(@RequestBody SortDto sortDto) {
    Sort convertedSort = sortConverter.convertDtoToSort(sortDto);
    convertedSort.setClassName("LabProblem");
    List<LabProblem> problems = labProblemService.getAllLabProblemsSorted(convertedSort);
    log.trace("getLabProblemsSorted call - params = SortDto:{}", sortDto);
    return new LabProblemsDto(labProblemConverter.convertModelsToDtos(problems));
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.PUT)
  ResponseDto updateLabProblem(@PathVariable Long id, @RequestBody LabProblemDto labProblemDto) {
    log.trace("updateLabProblem call - params = labProblemDto:{}", labProblemDto);
    try {
      if (labProblemService.updateLabProblem(
          id, labProblemConverter.convertDtoToModel(labProblemDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.GET)
  LabProblemDto getLabProblem(@PathVariable Long id) {
    log.trace("getLabProblem call - params = id:{}", id);
    return labProblemConverter.convertModelToDto(labProblemService.getLabProblem(id));
  }

  @RequestMapping(value = "/labs/{id}", method = RequestMethod.DELETE)
  ResponseDto deleteLabProblem(@PathVariable Long id) {
    log.trace("deleteLabProblem call - params = id:{}", id);
    try {
      if (labProblemService.deleteLabProblem(id)) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

}
