package com.example.umc10th_kaito.global.security.service;

import com.example.umc10th_kaito.domain.user.enums.SocialType;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import com.example.umc10th_kaito.global.security.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
 - Spring Security의 UserDetailsService 구현체
 - 로그인 시 Security가 이 서비스를 통해 DB에서 유저를 조회함
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /*
     - Security가 로그인 시 자동으로 호출
     - username = 로그인 폼에서 입력한 이메일
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(AuthUser::new)           // User 엔티티를 AuthUser로 감싸서 반환
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
    }

    // JWT 필터에서 사용 (socialType + socialUid로 조회)
    public UserDetails loadUserByUidAndSocialType(SocialType socialType, String uid) {
        return userRepository.findBySocialTypeAndSocialUid(socialType, uid)
                .map(AuthUser::new)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
    }
}
