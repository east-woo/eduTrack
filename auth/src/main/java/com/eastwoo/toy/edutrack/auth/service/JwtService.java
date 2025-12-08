package com.eastwoo.toy.edutrack.auth.service;

import com.eastwoo.toy.edutrack.auth.enumtype.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

/**
 * JwtService
 *
 * JSON Web Token(JWT)를 처리하는 서비스 클래스입니다.
 * 이 클래스는 액세스 토큰과 리프레시 토큰을 생성하고,
 * 기존 토큰을 파싱하거나 유효성을 검증하는 메서드를 제공합니다.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.access}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpiration;

    /**
     * 서명 키를 생성하는 메서드입니다.
     * 애플리케이션 설정 파일의 jwtSecret 값을 이용합니다.
     *
     * @return JWT 서명에 사용되는 Key 객체
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 특정 사용자를 위한 액세스 토큰을 생성하는 메서드입니다.
     *
     * @param userId 토큰을 생성할 사용자의 ID
     * @param role 사용자의 역할(Role)을 토큰의 클레임에 추가
     * @return 생성된 JWT 액세스 토큰 (문자열)
     */
    public String generateAccessToken(Long userId,  UserRole role) {
        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 ID를 주제로 설정
                .claim("role", role.name()) // 역할(Role)을 클레임에 추가
                .setIssuedAt(new Date()) // 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration)) // 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 서명을 포함
                .compact(); // 최종적으로 JWT 문자열 생성
    }

    /**
     * 특정 사용자를 위한 리프레시 토큰을 생성하는 메서드입니다.
     *
     * @param userId 토큰을 생성할 사용자의 ID
     * @return 생성된 JWT 리프레시 토큰 (문자열)
     */
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString()) // 사용자 ID를 주제로 설정
                .setIssuedAt(new Date()) // 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // 만료 시간
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 서명을 포함
                .compact(); // 최종적으로 JWT 문자열 생성
    }

    /**
     * 전달된 토큰을 파싱하고, 포함된 클레임 정보를 반환하는 메서드입니다.
     *
     * @param token 파싱할 JWT 토큰
     * @return 파싱된 토큰의 클레임 정보
     * @throws JwtException 토큰이 유효하지 않거나 만료된 경우 예외 발생
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 서명 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱
                .getBody(); // 클레임 내용 반환
    }
}