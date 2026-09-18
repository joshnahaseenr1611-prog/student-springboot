
package com.example.student.repository;

import com.example.student.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Integer> {

    boolean existsByStudentIdAndCourseId(
            int studentId,
            int courseId);
}