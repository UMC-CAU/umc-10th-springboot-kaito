package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security의 UserDetails 구현체
 * Security가 인증 과정에서 사용하는 사용자 정보 래퍼 클래스
 *
 * getUsername() → 로그인 식별자 (이메일)
 * getPassword() → BCrypt 암호화된 비밀번호
 * getAuthorities() → 권한 목록 (현재는 빈 리스트, 추후 Role 추가 가능)
 */
@Getter
@RequiredArgsConstructor
public class AuthUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // 현재 별도 권한 없음
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // 이메일을 로그인 식별자로 사용
    }
}
