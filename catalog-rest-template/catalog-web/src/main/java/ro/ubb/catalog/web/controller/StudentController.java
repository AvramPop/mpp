package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.web.converter.ConversionFactory;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.converter.StudentConverter;
import ro.ubb.catalog.web.dto.*;

/** Created by radu. */
@RestController
public class StudentController {
  public static final Logger log = LoggerFactory.getLogger(StudentController.class);

  @Autowired private StudentService studentService;

  @Autowired private StudentConverter studentConverter;
  @Autowired private ConversionFactory conversionFactory;

  @Autowired private SortConverter sortConverter;

  @RequestMapping(value = "/students", method = RequestMethod.GET)
  StudentsDto getStudents() {
    System.out.println("----------------");
    System.out.println("group number");
    studentService.findByGroupNumberCustom(2).forEach(System.out::println);
    System.out.println("name");
    studentService.findByNameCustom("dani").forEach(System.out::println);
    System.out.println("----------------");

    return new StudentsDto(studentConverter.convertModelsToDtos(studentService.getAllStudents()));
  }

  @RequestMapping(value = "/students/page/{pageNumber}/{perPage}", method = RequestMethod.GET)
  PagedStudentsDto getStudents(@PathVariable Integer pageNumber, @PathVariable Integer perPage) {
    log.trace("getStudents call - params:");
    return conversionFactory.convertPagedStudentsToDtos(studentService.getAllStudents(pageNumber, perPage));
  }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
  ResponseDto deleteStudent(@PathVariable Long id) {
    log.trace("deleteStudent call - params = id:{}", id);
    try {
      if (studentService.deleteStudent(id)) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/students/group/{groupNumber}", method = RequestMethod.GET)
  StudentsDto getStudentsFilteredByGroup(@PathVariable Integer groupNumber) {
    log.trace("getStudentsFilteredByGroup call - params = group:{}", groupNumber);
    return new StudentsDto(
        studentConverter.convertModelsToDtos(studentService.filterByGroup(groupNumber)));
  }

  @RequestMapping(value = "/students/sorted", method = RequestMethod.POST)
  StudentsDto getStudentsSorted(@RequestBody SortDto sort) {
    log.trace("getStudentsSorted call - params = sort:{}", sort);
    return new StudentsDto(
        studentConverter.convertModelsToDtos(
            studentService.getAllStudentsSorted(sortConverter.convertDtoToSort(sort))));
  }

  @RequestMapping(value = "/students", method = RequestMethod.POST)
  ResponseDto saveStudent(@RequestBody StudentDto studentDto) {
    log.trace("saveStudent call - params = new Student:{}", studentDto);
    try {
      if (studentService.saveStudent(studentConverter.convertDtoToModel(studentDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.PUT)
  ResponseDto updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
    log.trace("updateStudent call - params = up Student:{}", studentDto);
    try {
      if (studentService.updateStudent(id, studentConverter.convertDtoToModel(studentDto))) {
        return new ResponseDto(200);
      } else {
        return new ResponseDto(404);
      }
    } catch (Exception e) {
      return new ResponseDto(404);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
  StudentDto getStudent(@PathVariable Long id) {
    log.trace("getStudent call - params = id:{}", id);
    return studentConverter.convertModelToDto(studentService.getStudentById(id));
  }
}
