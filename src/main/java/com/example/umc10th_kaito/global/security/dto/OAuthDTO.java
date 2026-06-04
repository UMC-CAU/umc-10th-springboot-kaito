package com.example.umc10th_kaito.global.security.dto;

import com.example.umc10th_kaito.domain.user.enums.SocialType;

public interface OAuthDTO {
    SocialType getSocialType(); // 어떤 소셜 제공자인지(KAKAO, NAVER, GOOGLE 등)
    String getSocialUid(); // 소셜 제공자의 고유 ID
    String getSocialEmail(); // 이메일
    String getName(); // 닉네임
}
