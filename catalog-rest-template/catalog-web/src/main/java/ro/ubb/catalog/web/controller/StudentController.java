package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.converter.StudentConverter;
import ro.ubb.catalog.web.dto.ResponseDto;
import ro.ubb.catalog.web.dto.SortDto;
import ro.ubb.catalog.web.dto.StudentDto;
import ro.ubb.catalog.web.dto.StudentsDto;

/** Created by radu. */
@RestController
public class StudentController {
  public static final Logger log = LoggerFactory.getLogger(StudentController.class);

  @Autowired private StudentService studentService;

  @Autowired private StudentConverter studentConverter;

  @Autowired private SortConverter sortConverter;

  @RequestMapping(value = "/students", method = RequestMethod.GET)
  StudentsDto getStudents() {
    log.trace("getStudents call - params:");
    return new StudentsDto(studentConverter.convertModelsToDtos(studentService.getAllStudents()));
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
