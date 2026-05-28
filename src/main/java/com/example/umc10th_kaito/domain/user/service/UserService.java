package com.example.umc10th_kaito.domain.user.service;

import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import com.example.umc10th_kaito.global.security.AuthUser;
import com.example.umc10th_kaito.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String encoded = passwordEncoder.encode(request.getPassword());
        User user = UserConverter.toUser(request, encoded);
        return UserConverter.toRegisterResult(userRepository.save(user));
    }

    // 로그인 → JWT 토큰 발급
    @Transactional(readOnly = true)
    public UserResDTO.LoginResult login(UserReqDTO.Login request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ProjectException(UserErrorCode.INVALID_PASSWORD);
        }

        AuthUser authUser = new AuthUser(user);
        String accessToken = jwtUtil.createAccessToken(authUser);
        return UserConverter.toLoginResult(accessToken);
    }

    // 마이페이지 - 인증 객체에서 User 직접 추출 (DB 조회 불필요)
    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(AuthUser authUser) {
        return UserConverter.toMyPageResult(authUser.getUser());
    }
}
