package com.example.umc10th_kaito.domain.user.converter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import java.time.LocalDateTime;
public class UserConverter {
    public static User toUser(UserReqDTO.Register request, String encodedPassword) { // 1. DTO를 인자로 받아서 엔티티로 바꾸는 곳 (저장하기 위해)
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)      // BCrypt 암호화된 비밀번호 저장
                .name(request.getName())
                .socialType(SocialType.LOCAL)   // 임시 → LOCAL로 변경
                .address(request.getAddress())
                .build();
    }
    public static UserResDTO.RegisterResult toRegisterResult(User user) { // 2. 엔티티를 인자로 받아서 DTO로 바꾸는 곳 (클라이언트에게 응답하기 위해)
        return UserResDTO.RegisterResult.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static UserResDTO.MyPageResult toMyPageResult(User user) {
        return UserResDTO.MyPageResult.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalPoint(user.getTotalPoint())
                .build();
    }
}
