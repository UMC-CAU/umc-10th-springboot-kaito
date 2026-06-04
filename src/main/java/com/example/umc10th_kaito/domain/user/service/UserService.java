package com.example.umc10th_kaito.domain.user.service;
import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import com.example.umc10th_kaito.global.security.util.JwtUtil;
import com.example.umc10th_kaito.global.security.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  // 추가: JWT 토큰 발급을 위해 DI

    @Transactional
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = UserConverter.toUser(request, encodedPassword);
        return UserConverter.toRegisterResult(userRepository.save(user));
    }

    // 추가: 로그인 - 이메일/비밀번호 검증 후 JWT 발급
    @Transactional(readOnly = true)
    public UserResDTO.LoginResult login(UserReqDTO.Login request) {
        // 1. 이메일로 유저 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ProjectException(UserErrorCode.INVALID_PASSWORD);
        }

        // 3. JWT 토큰 발급
        AuthUser authUser = new AuthUser(user);
        String accessToken = jwtUtil.createAccessToken(authUser);
        return UserConverter.toLoginResult(accessToken);
    }

    // 개선: userId 대신 AuthUser에서 바로 User 꺼내서 반환 (DB 조회 불필요)
    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(AuthUser authUser) {
        return UserConverter.toMyPageResult(authUser.getUser());
    }
}
