package com.example.umc10th_kaito.domain.user.service;
import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = UserConverter.toUser(request);
        return UserConverter.toRegisterResult(userRepository.save(user));
    }
    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
        return UserConverter.toMyPageResult(user);
    }
}
