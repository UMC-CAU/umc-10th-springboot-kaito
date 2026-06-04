package com.example.umc10th_kaito.global.security.util;

import com.example.umc10th_kaito.domain.user.enums.SocialType;
import com.example.umc10th_kaito.global.security.entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Duration accessExpiration;

    public JwtUtil(
            @Value("${jwt.token.secretKey}") String secret,
            @Value("${jwt.token.expiration.access}") Long accessExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofMillis(accessExpiration);
    }

    // AccessToken 생성
    public String createAccessToken(AuthUser authUser) {
        return createToken(authUser, accessExpiration);
    }

    // 토큰에서 socialUid 가져오기
    public String getUid(String token) {
        try {
            return getClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }

    // 토큰에서 소셜 타입 가져오기
    public SocialType getSocialType(String token) {
        try {
            return SocialType.valueOf(
                    getClaims(token).getPayload().get("social_type").toString().toUpperCase()
            );
        } catch (JwtException e) {
            return null;
        }
    }

    // 토큰 유효성 확인
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 토큰 생성
    private String createToken(AuthUser authUser, Duration expiration) {
        Instant now = Instant.now();

        // 인가 정보
        String authorities = authUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder() // Payload 부분에 정보 담기
                .subject(authUser.getUser().getSocialUid())  // socialUid를 Subject로
                .claim("role", authorities) // .claim("role","USER") -> {"role":"USER"}
                .claim("email", authUser.getUsername())
                .issuedAt(Date.from(now)) // 언제 발급한지
                .expiration(Date.from(now.plus(expiration))) // 언제까지 유효한지, 여기까지가 페이로드 구성
                .signWith(secretKey) // 헤더 결정 (알고리즘 명시)
                .compact(); // 시그니처 생성 + 헤더,페이로드,시그니처 합쳐서 최종 JWT 문자열 반환
    }

    // 토큰 정보 가져오기
    private Jws<Claims> getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }
}
