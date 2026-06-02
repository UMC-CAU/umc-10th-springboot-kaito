package com.example.umc10th_kaito.domain.user.converter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import com.example.umc10th_kaito.global.security.dto.OAuthDTO;

import java.time.LocalDateTime;
public class UserConverter {
    public static User toUser(UserReqDTO.Register request, String encodedPassword) { // 1. DTO를 인자로 받아서 엔티티로 바꾸는 곳 (저장하기 위해)
        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)      // BCrypt 암호화된 비밀번호 저장
                .name(request.getName())
                .socialType(SocialType.LOCAL)   // 임시 → LOCAL로 변경
                .socialUid(request.getEmail())  // 추가: LOCAL 유저는 이메일을 socialUid로 사용
                .address(request.getAddress())
                .build();
    }

    // OAuth 소셜 로그인 유저 → User 엔티티 변환
    public static User toOAuthUser(OAuthDTO dto) {
        return User.builder()
                .email(dto.getSocialEmail())
                .password("")                    // 소셜 로그인은 비밀번호 없음
                .name(dto.getName())
                .socialType(dto.getSocialType())
                .socialUid(dto.getSocialUid())
                .build();
    }

    public static UserResDTO.RegisterResult toRegisterResult(User user) { // 2. 엔티티를 인자로 받아서 DTO로 바꾸는 곳 (클라이언트에게 응답하기 위해)
        return UserResDTO.RegisterResult.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 추가: accessToken 문자열 → LoginResult 객체 변환
    public static UserResDTO.LoginResult toLoginResult(String accessToken) {
        return UserResDTO.LoginResult.builder()
                .accessToken(accessToken)
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
