package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.global.ApiResponse;
import com.example.umc10th_kaito.global.BaseErrorCode;
import com.example.umc10th_kaito.global.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증(Authentication) 실패 핸들러
 * 로그인하지 않은 상태에서 Private API에 접근할 때 호출됨 (401 Unauthorized)
 * HTML 로그인 페이지 대신 응답 통일된 JSON을 내려줌
 */
@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BaseErrorCode code = GeneralErrorCode.UNAUTHORIZED;

        // 응답 형식 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code.getStatus().value());

        // 기존 ApiResponse 형식으로 통일해서 응답
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(code, null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
