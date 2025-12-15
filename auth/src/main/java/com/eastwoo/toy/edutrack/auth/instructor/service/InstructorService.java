package com.eastwoo.toy.edutrack.auth.instructor.service;

import com.eastwoo.toy.edutrack.auth.instructor.dto.InstructorSignupRequestDto;
import com.eastwoo.toy.edutrack.auth.instructor.dto.InviteTeacherRequest;
import com.eastwoo.toy.edutrack.auth.instructor.dto.TeacherRegisterRequest;
import com.eastwoo.toy.edutrack.auth.instructor.entity.InstructorPromotionRequest;
import com.eastwoo.toy.edutrack.auth.instructor.entity.InstructorSignupRequest;
import com.eastwoo.toy.edutrack.auth.invite.entity.InviteToken;
import com.eastwoo.toy.edutrack.auth.invite.service.InviteTokenService;
import com.eastwoo.toy.edutrack.auth.notification.port.NotificationPort;
import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserStatus;
import com.eastwoo.toy.edutrack.auth.global.exception.BusinessException;
import com.eastwoo.toy.edutrack.auth.global.exception.ErrorCode;
import com.eastwoo.toy.edutrack.auth.instructor.repository.InstructorPromotionRequestRepository;
import com.eastwoo.toy.edutrack.auth.instructor.repository.InstructorSignupRequestRepository;
import com.eastwoo.toy.edutrack.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class InstructorService {

    private final UserRepository userRepository;
    private final InviteTokenService inviteTokenService;
    private final InstructorSignupRequestRepository signupRequestRepository;
    private final InstructorPromotionRequestRepository promotionRequestRepository;
    private final NotificationPort notificationPort;
    private final PasswordEncoder passwordEncoder;


    /* 강사 회원가입 (초대 링크) */
    public User registerTeacher(TeacherRegisterRequest request) {

        InviteToken invite = inviteTokenService.validateToken(request.token());

        if (userRepository.existsByEmail(invite.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User teacher = User.builder()
                .name(request.name())
                .email(invite.getEmail())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.TEACHER)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(teacher);
        inviteTokenService.expireToken(invite);

        return teacher;
    }

    /* 관리자 → 강사 초대 */
    public void inviteTeacher(InviteTeacherRequest request) {
        String token = inviteTokenService.createInvite(
                request.email(),
                UserRole.TEACHER
        );

        notificationPort.sendInviteEmail(request.email(), token);
    }

    /* 강사 가입 요청 */
    public void requestSignup(InstructorSignupRequestDto request) {

        userRepository.findByEmail(request.email())
                .ifPresentOrElse(
                        user -> handleExistingUser(user, request.message()),
                        () -> handleGuestSignup(request)
                );
    }

    private void handleExistingUser(User user, String message) {

        if (user.getRole() == UserRole.TEACHER) {
            throw new BusinessException(ErrorCode.ALREADY_TEACHER);
        }

        requestPromotion(user, message);
    }

    private void handleGuestSignup(InstructorSignupRequestDto request) {

        if (signupRequestRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.SIGNUP_REQUEST_EXISTS);
        }

        signupRequestRepository.save(
                InstructorSignupRequest.create(
                        request.name(),
                        request.email(),
                        request.message()
                )
        );
    }

    /* =========================
       강사 승급 요청
       ========================= */
    public void requestPromotion(User user, String message) {

        if (promotionRequestRepository.existsByUser(user)) {
            throw new BusinessException(ErrorCode.PROMOTION_REQUEST_EXISTS);
        }

        promotionRequestRepository.save(
                InstructorPromotionRequest.create(user, message)
        );
    }

    /* =========================
       관리자 승인 (비회원 → 강사)
       ========================= */
    public void approveSignup(Long requestId) {

        InstructorSignupRequest request =
                signupRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.SIGNUP_REQUEST_NOT_FOUND)
                        );

        request.approve();

        String token = inviteTokenService.createInvite(
                request.getEmail(),
                UserRole.TEACHER
        );

        notificationPort.sendInviteEmail(request.getEmail(), token);
    }

    /* =========================
       관리자 거절 (가입 요청)
       ========================= */
    public void rejectSignup(Long requestId) {

        InstructorSignupRequest request =
                signupRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.SIGNUP_REQUEST_NOT_FOUND)
                        );

        request.reject();
    }

    /* =========================
       관리자 승인 (승급)
       ========================= */
    public void approvePromotion(Long requestId) {

        InstructorPromotionRequest request =
                promotionRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.PROMOTION_REQUEST_NOT_FOUND)
                        );

        request.approve();
        request.getUser().setRole(UserRole.TEACHER);
    }

    /* =========================
       관리자 거절 (승급)
       ========================= */
    public void rejectPromotion(Long requestId) {

        InstructorPromotionRequest request =
                promotionRequestRepository.findById(requestId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.PROMOTION_REQUEST_NOT_FOUND)
                        );

        request.reject();
    }
}
