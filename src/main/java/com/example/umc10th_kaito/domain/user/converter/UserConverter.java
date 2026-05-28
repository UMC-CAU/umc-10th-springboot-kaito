package com.example.umc10th_kaito.domain.user.converter;

import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;

import java.time.LocalDateTime;

public class UserConverter {

    // Register DTO → User 엔티티
    public static User toUser(UserReqDTO.Register request, String encodedPassword) {
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .socialType(SocialType.LOCAL)
                .socialUid(request.getEmail())  // LOCAL 유저는 이메일을 socialUid로 사용
                .address(request.getAddress())
                .build();
    }

    // User 엔티티 → RegisterResult DTO
    public static UserResDTO.RegisterResult toRegisterResult(User user) {
        return UserResDTO.RegisterResult.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // accessToken String → LoginResult DTO
    public static UserResDTO.LoginResult toLoginResult(String accessToken) {
        return UserResDTO.LoginResult.builder()
                .accessToken(accessToken)
                .build();
    }

    // User 엔티티 → MyPageResult DTO
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
