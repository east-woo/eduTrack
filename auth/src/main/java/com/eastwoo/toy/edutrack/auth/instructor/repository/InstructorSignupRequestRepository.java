package com.eastwoo.toy.edutrack.auth.instructor.repository;

import com.eastwoo.toy.edutrack.auth.instructor.entity.InstructorSignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorSignupRequestRepository extends JpaRepository<InstructorSignupRequest, Long> {
    /*중복 요청 방지*/
    boolean existsByEmail(String email);
}