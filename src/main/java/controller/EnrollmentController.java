
package com.example.student.controller;

import com.example.student.entity.Enrollment;
import com.example.student.repository.EnrollmentRepository;
import com.example.student.repository.StudentRepository;
import com.example.student.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // GET ALL ENROLLMENTS
    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    // GET ENROLLMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable int id) {

        Enrollment enrollment =
                enrollmentRepository.findById(id).orElse(null);

        if (enrollment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enrollment not found");
        }

        return ResponseEntity.ok(enrollment);
    }

    // CREATE ENROLLMENT
    @PostMapping
    public ResponseEntity<?> createEnrollment(
            @RequestBody Enrollment enrollment) {

        // CHECK STUDENT
        if (!studentRepository.existsById(enrollment.getStudentId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        // CHECK COURSE
        if (!courseRepository.existsById(enrollment.getCourseId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Course not found");
        }

        // CHECK DUPLICATE ENROLLMENT
        if (enrollmentRepository.existsByStudentIdAndCourseId(
                enrollment.getStudentId(),
                enrollment.getCourseId())) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Student already enrolled");
        }

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEnrollment);
    }

    // UPDATE ENROLLMENT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEnrollment(
            @PathVariable int id,
            @RequestBody Enrollment enrollment) {

        Enrollment existingEnrollment =
                enrollmentRepository.findById(id).orElse(null);

        if (existingEnrollment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enrollment not found");
        }

        existingEnrollment.setStudentId(enrollment.getStudentId());
        existingEnrollment.setCourseId(enrollment.getCourseId());
        existingEnrollment.setEnrollmentDate(
                enrollment.getEnrollmentDate());
        existingEnrollment.setStatus(enrollment.getStatus());

        Enrollment updatedEnrollment =
                enrollmentRepository.save(existingEnrollment);

        return ResponseEntity.ok(updatedEnrollment);
    }

    // DELETE ENROLLMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable int id) {

        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Enrollment not found");
        }

        enrollmentRepository.deleteById(id);

        return ResponseEntity.ok("Enrollment deleted successfully");
    }
}