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
@Component  // Spring이 관리하는 객체로 등록해야 SecurityConfig에서 DI받을 수 있음.
public class CustomEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, // 1번 파라미터, 클라이언트가 보낸 요청 정보 (어떤 URL로 왔는지, 헤더가 뭔지)
            HttpServletResponse response, // 2번 파라미터, 클라이언트에게 보낼 응답 객체 (우리 코드에서 401이랑 객체 담는 데 사용)
            AuthenticationException authException // 3번 파라미터, 어떤 인증 예외가 발생했는지에 대해서 (우리코드에서 사용x)
    ) throws IOException {
        // SecurityResponsUtil.java 추가함으로써 코드 단축
        SecurityResponseUtil.writeErrorResponse(response, GeneralErrorCode.UNAUTHORIZED);
    }
}

/*
로그인 안 한 상태로 Private API 접근
        ↓
Spring Security가 commence() 자동 호출
        ↓
response에 401 상태코드 + ApiResponse JSON 담기
        ↓
클라이언트에게 JSON으로 응답
 */