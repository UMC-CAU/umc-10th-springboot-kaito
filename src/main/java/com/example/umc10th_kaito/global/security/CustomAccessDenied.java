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
    public void handle( // 인가 실패 시 Spring이 호출
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        // SecurityResponsUtil.java 추가함으로써 코드 단축
        SecurityResponseUtil.writeErrorResponse(response, GeneralErrorCode.FORBIDDEN);
    }
}
