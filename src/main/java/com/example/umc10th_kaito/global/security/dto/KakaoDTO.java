package com.example.umc10th_kaito.global.security.dto;

import com.example.umc10th_kaito.domain.user.enums.SocialType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoDTO implements OAuthDTO {

    private final String id;        // 카카오 고유 ID
    private final String email;     // 카카오 이메일
    private final String name;      // 카카오 닉네임

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getSocialUid() {
        return id;
    }

    @Override
    public String getSocialEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }
}
