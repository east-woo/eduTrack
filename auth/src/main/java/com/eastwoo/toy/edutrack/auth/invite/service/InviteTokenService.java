package com.eastwoo.toy.edutrack.auth.invite.service;

import com.eastwoo.toy.edutrack.auth.invite.entity.InviteToken;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.global.exception.BusinessException;
import com.eastwoo.toy.edutrack.auth.global.exception.ErrorCode;
import com.eastwoo.toy.edutrack.auth.invite.repository.InviteTokenRepository;
import com.eastwoo.toy.edutrack.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTokenService {

    private final InviteTokenRepository inviteTokenRepository;
    private final UserRepository userRepository;

    /*토큰 생성*/
    public String createInvite(String email, UserRole role) {

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        inviteTokenRepository.findByEmail(email)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .ifPresent(t -> {
                    throw new BusinessException(ErrorCode.INVITE_ALREADY_EXISTS);
                });

        String token = UUID.randomUUID().toString();

        InviteToken invite = InviteToken.builder()
                .email(email)
                .token(token)
                .role(role)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        inviteTokenRepository.save(invite);
        return token;
    }

    public InviteToken validateToken(String token) {
        InviteToken invite = inviteTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITE_TOKEN_INVALID));

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.INVITE_TOKEN_EXPIRED);
        }

        return invite;
    }

    public void expireToken(InviteToken inviteToken) {
        inviteTokenRepository.delete(inviteToken);
    }
}