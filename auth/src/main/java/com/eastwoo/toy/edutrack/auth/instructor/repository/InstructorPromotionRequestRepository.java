package com.eastwoo.toy.edutrack.auth.instructor.repository;

import com.eastwoo.toy.edutrack.auth.instructor.entity.InstructorPromotionRequest;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*등급 업그레이드*/
@Repository
public interface InstructorPromotionRequestRepository
        extends JpaRepository<InstructorPromotionRequest, Long> {

    boolean existsByUser(User user);
}