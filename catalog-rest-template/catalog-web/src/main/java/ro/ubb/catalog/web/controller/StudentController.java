package ro.ubb.catalog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.web.converter.SortConverter;
import ro.ubb.catalog.web.converter.StudentConverter;
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
    // todo: log
    return new StudentsDto(studentConverter.convertModelsToDtos(studentService.getAllStudents()));
  }

  @RequestMapping(value = "/students/group/{groupNumber}", method = RequestMethod.GET)
  StudentsDto getStudentsFilteredByGroup(@PathVariable Integer groupNumber) {
    // todo: log
    return new StudentsDto(
        studentConverter.convertModelsToDtos(studentService.filterByGroup(groupNumber)));
  }

  @RequestMapping(value = "/students/sorted", method = RequestMethod.GET)
  StudentsDto getStudentsSorted(@RequestBody SortDto sort) {
    // todo: log
    return new StudentsDto(
        studentConverter.convertModelsToDtos(
            studentService.getAllStudentsSorted(sortConverter.convertDtoToSort(sort))));
  }

  @RequestMapping(value = "/students", method = RequestMethod.POST)
  ResponseEntity<?> saveStudent(@RequestBody StudentDto studentDto) {
    // todo log
    if (studentService.saveStudent(studentConverter.convertDtoToModel(studentDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.PUT)
  ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {

    if (studentService.updateStudent(id, studentConverter.convertDtoToModel(studentDto))) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
  StudentDto getStudent(@PathVariable Long id) {
    // todo: log
    return studentConverter.convertModelToDto(studentService.getStudentById(id));
  }
}
