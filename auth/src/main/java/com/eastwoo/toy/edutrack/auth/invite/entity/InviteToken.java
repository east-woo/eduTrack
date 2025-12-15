package com.eastwoo.toy.edutrack.auth.invite.entity;

import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 강사 초대 요청 저장
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(unique = true)
    private String token;

    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}