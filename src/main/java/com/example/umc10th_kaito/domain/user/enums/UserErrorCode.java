package com.example.umc10th_kaito.domain.user.enums;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "해당 유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER400_1", "이미 존재하는 이메일입니다.");
    private final HttpStatus status; // @AllArgsConstructor로 인해 생성된 생성자에서 초기화됩니다.
    private final String code;
    private final String message;
}
