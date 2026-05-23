package com.example.umc10th_kaito.domain.user;

import com.example.umc10th_kaito.global.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에서 Bean으로 등록한 BCryptPasswordEncoder

    /**
     * 회원가입
     * 1. 이메일 중복 체크
     * 2. 비밀번호 BCrypt 암호화
     * 3. User 엔티티 생성 & DB 저장
     */
    @Transactional
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // BCrypt로 비밀번호 암호화 (솔트 처리)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = UserConverter.toUser(request, encodedPassword);
        return UserConverter.toRegisterResult(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
        return UserConverter.toMyPageResult(user);
    }
}
