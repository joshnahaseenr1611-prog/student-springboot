
package com.example.student.controller;

import com.example.student.entity.Studententity;
import com.example.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StuController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. Get all students
    @GetMapping
    public List<Studententity> getAllStudents() {
        return studentRepository.findAll();
    }

    // 2. Hello message
    @GetMapping("/hello")
    public String hello() {
        return "welcome to spring class";
    }

    // 3. Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Studententity> getStudentById(
            @PathVariable int id) {

        Optional<Studententity> student =
                studentRepository.findById(id);

        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Create a new student
    @PostMapping
    public ResponseEntity<Studententity> createStudent(
            @RequestBody Studententity student) {

        Studententity savedStudent =
                studentRepository.save(student);

        return new ResponseEntity<>(
                savedStudent, HttpStatus.CREATED);
    }

    // 5. Update an existing student
    @PutMapping("/{id}")
    public ResponseEntity<Studententity> updateStudent(
            @PathVariable int id,
            @RequestBody Studententity studentDetails) {

        Optional<Studententity> optionalStudent =
                studentRepository.findById(id);

        if (optionalStudent.isPresent()) {

            Studententity student = optionalStudent.get();

            student.setName(studentDetails.getName());
            student.setDepartment(studentDetails.getDepartment());
            student.setAge(studentDetails.getAge());

            Studententity updatedStudent =
                    studentRepository.save(student);

            return ResponseEntity.ok(updatedStudent);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 6. Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable int id) {

        if (studentRepository.existsById(id)) {

            studentRepository.deleteById(id);

            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}