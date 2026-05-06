package com.example.umc10th_kaito.domain.review.enums;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REVIEW400_1", "이미 리뷰를 작성한 미션입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
