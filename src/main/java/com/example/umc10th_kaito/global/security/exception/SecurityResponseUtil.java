package com.example.umc10th_kaito.global.security.exception;

import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SecurityResponseUtil {

    public static void writeErrorResponse(
            HttpServletResponse response,
            BaseErrorCode code) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code.getStatus().value());
        ApiResponse<Void> errorResponse = ApiResponse.onFailure(code, null);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
