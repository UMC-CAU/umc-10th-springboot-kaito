package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.domain.user.UserErrorCode;
import com.example.umc10th_kaito.domain.user.UserRepository;
import com.example.umc10th_kaito.global.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 UserDetailsService 구현체
 * AuthenticationProvider가 인증 시 이 서비스를 통해 DB에서 사용자 정보를 조회함
 *
 * loadUserByUsername(이메일) → DB에서 User 조회 → AuthUser(UserDetails)로 반환
 * 이후 Security가 AuthUser의 getPassword()와 입력된 비밀번호를 BCrypt로 비교
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = 로그인 폼에서 입력한 이메일
        return userRepository.findByEmail(username)
                .map(AuthUser::new)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
    }
}
