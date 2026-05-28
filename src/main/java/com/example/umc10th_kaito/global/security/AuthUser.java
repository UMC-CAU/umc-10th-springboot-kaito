package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
 - Spring Security의 UserDetails 구현체
 - User 엔티티를 Security 인증 객체로 래핑
 */
@Getter
@RequiredArgsConstructor
public class AuthUser implements UserDetails {

    private final User user;

    // 권한 목록 반환 (현재는 권한 구분 없으므로 빈 리스트)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // Security가 비밀번호 검증에 사용할 값 → BCrypt 암호화된 비밀번호
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Security가 로그인 식별자로 사용할 값 → 이메일
    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
