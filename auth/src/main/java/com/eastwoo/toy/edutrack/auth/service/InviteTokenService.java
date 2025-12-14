package com.eastwoo.toy.edutrack.auth.service;

import com.eastwoo.toy.edutrack.auth.entity.InviteToken;
import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import com.eastwoo.toy.edutrack.auth.repository.InviteTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteTokenService {

    private final InviteTokenRepository inviteTokenRepository;

    public String createInvite(String email, UserRole role) {

        String token = UUID.randomUUID().toString();

        InviteToken invite = InviteToken.builder()
                .email(email)
                .token(token)
                .role(role) // TEACHER
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        inviteTokenRepository.save(invite);

        return token;
    }


    public InviteToken validateToken(String token) {
        InviteToken invite = inviteTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 초대 토큰"));

        if (invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("초대 토큰이 만료되었습니다.");
        }

        return invite;
    }

    public void expireToken(InviteToken inviteToken) {
        inviteTokenRepository.delete(inviteToken);
    }
}