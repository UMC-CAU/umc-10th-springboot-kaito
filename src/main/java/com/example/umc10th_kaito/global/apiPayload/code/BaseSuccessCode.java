package com.example.umc10th_kaito.global.apiPayload.code;
import org.springframework.http.HttpStatus;
public interface BaseSuccessCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
