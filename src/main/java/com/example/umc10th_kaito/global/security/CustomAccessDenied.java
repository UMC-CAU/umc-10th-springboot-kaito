package com.example.umc10th_kaito.global.security;

import com.example.umc10th_kaito.global.ApiResponse;
import com.example.umc10th_kaito.global.BaseErrorCode;
import com.example.umc10th_kaito.global.GeneralErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인가(Authorization) 실패 핸들러
 * 로그인은 했지만 해당 리소스에 접근 권한이 없을 때 호출됨 (403 Forbidden)
 * 기존 GeneralExceptionAdvice로는 필터 단의 예외를 잡지 못하기 때문에 별도 구현
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

        // 기존 ApiResponse 형식으로 통일해서 응답
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(code, null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
