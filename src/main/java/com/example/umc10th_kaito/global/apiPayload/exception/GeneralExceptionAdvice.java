package com.example.umc10th_kaito.global.apiPayload.exception;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice // 스프링 전체에서 발생하는 Exception을 이 클래스가 잡겠다는 선언
public class GeneralExceptionAdvice {
    @ExceptionHandler(ProjectException.class) // ProjectException 타입의 모든 Exception을 낚아챔.
    public ResponseEntity<ApiResponse<Void>> handleProjectException(ProjectException e) { // 낚아챈 예외객체(e)를 인자로 받겠다. 이 안에 에러콛나 메세지,에러코드가 있음.
        BaseErrorCode errorCode = e.getErrorCode(); // e 안에 담겨있는 구체적인 에러정보(ex. USER_NOT_FOUND)를 꺼냄.
        return ResponseEntity.status(errorCode.getStatus()) // 클라이언트에게 보낼 HTTP 상태 코드를 설정 (ex.400, 404)
                .body(ApiResponse.onFailure(errorCode, null)); // 방금 준비한 ResponseEntity라는 봉투 안에 이 내용물(body)을 담아줘
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        BaseErrorCode code = GeneralErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.onFailure(code, e.getMessage()));
    }
}
