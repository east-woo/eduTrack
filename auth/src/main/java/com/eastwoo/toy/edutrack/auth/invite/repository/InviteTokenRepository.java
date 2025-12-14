package com.eastwoo.toy.edutrack.auth.invite.repository;

import com.eastwoo.toy.edutrack.auth.invite.entity.InviteToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteTokenRepository extends JpaRepository<InviteToken, Long> {

    /*초대 링크 클릭시 토큰 기반 조회*/
    Optional<InviteToken> findByToken(String token);

    /*이메일로 강사 초대 요청 확인 */
    Optional<InviteToken> findByEmail(String email);
}