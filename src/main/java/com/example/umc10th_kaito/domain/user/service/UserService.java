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
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) { // JSON 데이터를 DTO 객체로 받는다.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = UserConverter.toUser(request); // 받은 DTO객체를 엔티티로 변환한다.
        return UserConverter.toRegisterResult(userRepository.save(user));
    }
    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND)); // 이 줄이 실행되는 순간, 메모리에 ProjectException 객체가 생성되고, 그 객체는 UserErrorCode.USER_NOT_FOUND를 포함하게 된다. 그리고 나서 throw 키워드에 의해 이 예외가 즉시 던져진다. 이 예외는 호출 스택을 따라 올라가면서 적절한 예외 처리기에 의해 처리될 때까지 전파된다.
        return UserConverter.toMyPageResult(user);
    }
}
