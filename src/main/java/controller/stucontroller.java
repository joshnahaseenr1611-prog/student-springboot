package com.example.student.controller;

import com.example.student.entity.Studententity;
import com.example.student.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StuController {

    @Autowired
    private StudentRepository studentRepository;

    // GET all students
    @GetMapping
    public List<Studententity> getAllStudents() {
        return studentRepository.findAll();
    }

    // GET student by ID
    @GetMapping("/{id}")
    public Studententity getStudentById(@PathVariable int id) {
        return studentRepository.findById(id).orElse(null);
    }

    // UPDATE student
    @PutMapping("/{id}")
    public Studententity updateStudent(
            @PathVariable int id,
            @RequestBody Studententity student) {

        Studententity existingStudent =
                studentRepository.findById(id).orElse(null);

        if (existingStudent == null) {
            return null;
        }

        existingStudent.setName(student.getName());
        existingStudent.setDepartment(student.getDepartment());
        existingStudent.setAge(student.getAge());
        existingStudent.setUsername(student.getUsername());
        existingStudent.setPassword(student.getPassword());

        return studentRepository.save(existingStudent);
    }

    // DELETE student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {

        studentRepository.deleteById(id);

        return "Student deleted successfully";
    }
}