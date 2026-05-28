package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 - 인가 실패 핸들러
 - 로그인은 했지만 해당 리소스에 접근 권한이 없을 때 호출됨
 - 403 + ApiResponse JSON으로 응답
 */
@Component
public class CustomAccessDenied implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BaseErrorCode code = GeneralErrorCode.FORBIDDEN;

        // 응답 형식 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code.getStatus().value());

        // ApiResponse 형식으로 응답 통일
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(code, null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
