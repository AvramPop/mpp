package ro.ubb.catalog.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ubb.catalog.core.model.Assignment;
import ro.ubb.catalog.core.model.LabProblem;
import ro.ubb.catalog.core.model.Student;
import ro.ubb.catalog.core.service.LabProblemService;
import ro.ubb.catalog.core.service.StudentService;
import ro.ubb.catalog.web.dto.AssignmentDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class AssignmentConverter extends BaseConverter<Assignment, AssignmentDto> {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Assignment convertDtoToModel(AssignmentDto dto) {
    System.out.println(dto);
    Assignment assignment =
        Assignment.builder()
            .grade(dto.getGrade())
            .student(entityManager.getReference(Student.class, dto.getStudentId()))
            .labProblem(entityManager.getReference(LabProblem.class, dto.getLabProblemId()))
            .build();
    assignment.setId(dto.getId());
    return assignment;
  }

  @Override
  public AssignmentDto convertModelToDto(Assignment assignment) {
    AssignmentDto dto =
        AssignmentDto.builder()
            .grade(assignment.getGrade())
            .studentId(assignment.getStudent().getId())
            .labProblemId(assignment.getLabProblem().getId())
            .build();
    dto.setId(assignment.getId());
    return dto;
  }
}
