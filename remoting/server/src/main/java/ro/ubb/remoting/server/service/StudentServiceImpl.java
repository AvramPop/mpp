package ro.ubb.remoting.server.service;

import ro.ubb.remoting.common.domain.Student;
import ro.ubb.remoting.common.domain.exceptions.ValidatorException;
import ro.ubb.remoting.common.service.StudentService;
import ro.ubb.remoting.common.service.sort.Sort;

import java.util.*;

/**
 * Created by radu.
 */
public class StudentServiceImpl implements StudentService {
    @Override
    public Optional<Student> addStudent(Long id, String serialNumber, String name, int group) throws ValidatorException{
        return Optional.empty();
    }

    @Override
    public Set<Student> getAllStudents() {
        Student s1 = new Student("sn1", "dani", 5);
        Student s2 = new Student("sn2", "Tamas", 6);
        Set<Student> set = new HashSet<>();
        set.add(s1);
        set.add(s2);
        return set;
    }

    @Override
    public List<Student> getAllStudentsSorted(Sort sort){
        return null;
    }

    @Override
    public Optional<Student> deleteStudent(Long id){
        return Optional.empty();
    }

    @Override
    public Optional<Student> updateStudent(Long id, String serialNumber, String name, int group) throws ValidatorException{
        return Optional.empty();
    }

    @Override
    public Set<Student> filterByGroup(Integer group){
        return null;
    }

    @Override
    public Optional<Student> getStudentById(Long id){
        return Optional.empty();
    }
}

