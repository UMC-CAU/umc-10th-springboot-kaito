package com.example.umc10th_kaito.domain.review.enums;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements BaseErrorCode {
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "REVIEW400_1", "이미 리뷰를 작성한 미션입니다."),
    INVALID_QUERY(HttpStatus.BAD_REQUEST, "REVIEW400_2", "유효하지 않은 쿼리 타입입니다. (id 또는 score만 허용)");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
