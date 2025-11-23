package com.eastwoo.toy.edutrack.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.eastwoo.toy.edutrack.auth.dto
 * fileName       : AuthResponse
 * author         : dongwoo
 * date           : 2025-11-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-11-22        dongwoo       최초 생성
 */
@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
