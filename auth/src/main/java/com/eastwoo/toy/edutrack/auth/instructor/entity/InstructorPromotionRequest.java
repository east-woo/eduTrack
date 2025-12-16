package com.eastwoo.toy.edutrack.auth.instructor.entity;

import com.eastwoo.toy.edutrack.auth.instructor.enumtype.SignupRequestStatus;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 강사승진요청
@Entity
@Table(name = "instructor_promotion_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InstructorPromotionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 이미 존재하는 User */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SignupRequestStatus status = SignupRequestStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static InstructorPromotionRequest create(User user, String message) {
        return InstructorPromotionRequest.builder()
                .user(user)
                .message(message)
                .build();
    }

    private void validatePending() {
        if (this.status != SignupRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 강사 승급 요청입니다.");
        }
    }

    public void approve() {
        validatePending();
        this.status = SignupRequestStatus.APPROVED;
    }

    public void reject() {
        validatePending();
        this.status = SignupRequestStatus.REJECTED;
    }
}