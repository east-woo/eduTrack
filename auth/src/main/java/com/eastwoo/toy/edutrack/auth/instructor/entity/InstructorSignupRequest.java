package com.eastwoo.toy.edutrack.auth.instructor.entity;

import com.eastwoo.toy.edutrack.auth.instructor.enumtype.SignupRequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*회원 가입 요청*/
@Entity
@Table(name = "instructor_signup_requests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InstructorSignupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SignupRequestStatus status = SignupRequestStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public void approve() {
        if (this.status != SignupRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }
        this.status = SignupRequestStatus.APPROVED;
    }

    public void reject() {
        if (this.status != SignupRequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }
        this.status = SignupRequestStatus.REJECTED;
    }

    /* 정적 팩토리 */
    public static InstructorSignupRequest create(String name, String email, String message) {
        return InstructorSignupRequest.builder()
                .name(name)
                .email(email)
                .message(message)
                .build();
    }

}