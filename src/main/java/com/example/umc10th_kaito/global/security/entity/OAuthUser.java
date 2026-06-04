package com.example.umc10th_kaito.global.security.entity;

import com.example.umc10th_kaito.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class OAuthUser implements OAuth2User {

    private final User user;                          // 우리 DB의 User 엔티티
    private final Map<String, Object> attributes;     // 카카오 원본 사용자 정보

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return user.getSocialUid();   // 소셜 UID를 이름으로 반환
    }
}
