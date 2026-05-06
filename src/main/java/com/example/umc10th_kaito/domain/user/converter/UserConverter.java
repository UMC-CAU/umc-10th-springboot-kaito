package com.example.umc10th_kaito.domain.user.converter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import java.time.LocalDateTime;
public class UserConverter {
    public static User toUser(UserReqDTO.Register request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .socialType(SocialType.KAKAO) // 임시 (소셜 로그인 미구현)
                .socialUid(request.getEmail()) // 임시
                .address(request.getAddress())
                .build();
    }
    public static UserResDTO.RegisterResult toRegisterResult(User user) {
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
