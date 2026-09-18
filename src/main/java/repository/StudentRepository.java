package com.example.student.repository;

import com.example.student.entity.Studententity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository
        extends JpaRepository<Studententity, Integer> {

    boolean existsByUsername(String username);

    Optional<Studententity> findByUsername(String username);
}