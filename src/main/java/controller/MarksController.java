
package com.example.student.controller;

import com.example.student.entity.Marks;
import com.example.student.repository.MarksRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marks")
public class MarksController {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET ALL MARKS
    @GetMapping
    public List<Marks> getAllMarks() {
        return marksRepository.findAll();
    }

    // GET MARKS BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMarksById(@PathVariable int id) {

        Marks marks = marksRepository.findById(id).orElse(null);

        if (marks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Marks not found");
        }

        return ResponseEntity.ok(marks);
    }

    // ADD MARKS
    @PostMapping
    public ResponseEntity<?> addMarks(@RequestBody Marks marks) {

        // CHECK STUDENT
        if (!studentRepository.existsById(marks.getStudentId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        // CHECK COURSE
        if (!courseRepository.existsById(marks.getCourseId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Course not found");
        }

        // CHECK NEGATIVE MARKS
        if (marks.getMarks() < 0) {
            return ResponseEntity.badRequest()
                    .body("Marks cannot be negative");
        }

        // CHECK TOTAL MARKS
        if (marks.getTotalMarks() <= 0) {
            return ResponseEntity.badRequest()
                    .body("Total marks must be greater than zero");
        }

        // CHECK MARKS GREATER THAN TOTAL
        if (marks.getMarks() > marks.getTotalMarks()) {
            return ResponseEntity.badRequest()
                    .body("Marks cannot be greater than total marks");
        }

        Marks savedMarks = marksRepository.save(marks);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedMarks);
    }

    // UPDATE MARKS
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMarks(
            @PathVariable int id,
            @RequestBody Marks marks) {

        Marks existingMarks = marksRepository.findById(id).orElse(null);

        if (existingMarks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Marks not found");
        }

        // CHECK STUDENT
        if (!studentRepository.existsById(marks.getStudentId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        // CHECK COURSE
        if (!courseRepository.existsById(marks.getCourseId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Course not found");
        }

        // CHECK NEGATIVE MARKS
        if (marks.getMarks() < 0) {
            return ResponseEntity.badRequest()
                    .body("Marks cannot be negative");
        }

        // CHECK TOTAL MARKS
        if (marks.getTotalMarks() <= 0) {
            return ResponseEntity.badRequest()
                    .body("Total marks must be greater than zero");
        }

        // CHECK MARKS GREATER THAN TOTAL
        if (marks.getMarks() > marks.getTotalMarks()) {
            return ResponseEntity.badRequest()
                    .body("Marks cannot be greater than total marks");
        }

        existingMarks.setStudentId(marks.getStudentId());
        existingMarks.setCourseId(marks.getCourseId());
        existingMarks.setExamName(marks.getExamName());
        existingMarks.setMarks(marks.getMarks());
        existingMarks.setTotalMarks(marks.getTotalMarks());

        Marks updatedMarks = marksRepository.save(existingMarks);

        return ResponseEntity.ok(updatedMarks);
    }

    // DELETE MARKS
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarks(@PathVariable int id) {

        if (!marksRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Marks not found");
        }

        marksRepository.deleteById(id);

        return ResponseEntity.ok("Marks deleted successfully");
    }
}