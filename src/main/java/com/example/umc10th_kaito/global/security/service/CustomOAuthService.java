package com.example.umc10th_kaito.global.security.service;

import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.security.dto.KakaoDTO;
import com.example.umc10th_kaito.global.security.dto.OAuthDTO;
import com.example.umc10th_kaito.global.security.entity.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuthService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 라이브러리가 카카오한테서 받아온 사용자 정보
        OAuth2User oAuthUser = super.loadUser(userRequest);

        // 2. 어떤 소셜 제공자인지 확인 (KAKAO, GOOGLE 등)
        SocialType socialType;
        String socialUid;
        try {
            socialType = SocialType.valueOf(
                    userRequest.getClientRegistration().getRegistrationId().toUpperCase()
            );
            socialUid = String.valueOf((Long) oAuthUser.getAttribute("id"));
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // 3. 카카오 응답에서 필요한 정보 추출
        Map<String, Object> attributes = oAuthUser.getAttribute("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        // 4. 공통 DTO로 변환
        OAuthDTO dto;
        switch (socialType) {
            case KAKAO -> {
                String email = attributes.get("email").toString();
                String name = profile.get("nickname").toString();
                dto = new KakaoDTO(socialUid, email, name);
            }
            default -> throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        // 5. DB 확인: 있으면 조회, 없으면 저장
        User user = userRepository.findBySocialTypeAndSocialUid(socialType, socialUid)
                .orElseGet(() -> {
                    User newUser = UserConverter.toOAuthUser(dto);
                    return userRepository.save(newUser);
                });

        // 6. OAuthUser로 감싸서 반환 (라이브러리한테)
        return new OAuthUser(user, oAuthUser.getAttributes());
    }
}
