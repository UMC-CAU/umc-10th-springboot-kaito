package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 -인증 실패 핸들러
 -로그인 안 한 상태로 Private API 접근 시 호출됨
 -HTML 로그인 페이지 대신 401 + ApiResponse JSON으로 응답
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

        // ApiResponse 형식으로 응답 통일
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(code, null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
