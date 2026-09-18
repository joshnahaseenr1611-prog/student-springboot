package com.example.student.controller;

import com.example.student.entity.Studententity;
import com.example.student.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentRepository studentRepository;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Studententity student) {

        if (student.getUsername() == null
                || student.getUsername().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Username is required");
        }

        if (student.getPassword() == null
                || student.getPassword().isBlank()) {

            return ResponseEntity.badRequest()
                    .body("Password is required");
        }

        if (studentRepository.existsByUsername(
                student.getUsername())) {

            return ResponseEntity.status(
                    HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        studentRepository.save(student);

        return ResponseEntity.status(
                HttpStatus.CREATED)
                .body("Registration Successful");
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Studententity loginStudent) {

        Studententity student =
                studentRepository.findByUsername(
                        loginStudent.getUsername())
                        .orElse(null);

        if (student == null) {
            return ResponseEntity.status(
                    HttpStatus.NOT_FOUND)
                    .body("Username not found");
        }

        if (!student.getPassword().equals(
                loginStudent.getPassword())) {

            return ResponseEntity.status(
                    HttpStatus.UNAUTHORIZED)
                    .body("Invalid Password");
        }

        Map<String, String> response =
                new HashMap<>();

        response.put("message", "Login Successful");
        response.put("studentName", student.getName());

        return ResponseEntity.ok(response);
    }
}