package com.example.umc10th_kaito.global.security.handler;

import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseSuccessCode;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import com.example.umc10th_kaito.global.security.entity.AuthUser;
import com.example.umc10th_kaito.global.security.entity.OAuthUser;
import com.example.umc10th_kaito.global.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        // 1. 사전 작업: ObjectMapper 선언
        ObjectMapper objectMapper = new ObjectMapper();
        BaseSuccessCode code = GeneralSuccessCode.OK;

        // 2. Content-Type, Status 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code.getStatus().value());

        // 3. SecurityContextHolder에서 OAuth 인증 객체 꺼내기
        OAuthUser oAuthUser = (OAuthUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        // 4. OAuthUser에서 User 꺼내서 AuthUser 만들고 JWT 발급
        String accessToken = jwtUtil.createAccessToken(new AuthUser(oAuthUser.getUser()));

        // 5. 응답 통일 객체로 래핑
        ApiResponse<UserResDTO.LoginResult> responseBody = ApiResponse.onSuccess(
                code,
                UserConverter.toLoginResult(accessToken)
        );

        // 6. 응답 출력
        objectMapper.writeValue(response.getOutputStream(), responseBody);
    }
}
