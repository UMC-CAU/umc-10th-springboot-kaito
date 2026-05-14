package com.example.umc10th_kaito.global.apiPayload.exception;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;
@Getter
public class ProjectException extends RuntimeException {
    private final BaseErrorCode errorCode; // @RequiredArgsConstructor 안 써도 되나 ?
    // 안써도된다. 바로 아래에서 생성자가 "직접" 작성돼있으니까.
    public ProjectException(BaseErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모클래스인 RuntimeException의 생성자를 호출하는것. RuntimeException의 생성자 중 하나는 String message를 받는 생성자이므로, errorCode.getMessage()를 전달하여 예외 메시지를 설정한다.
        this.errorCode = errorCode; // errorCode 필드에 저장
    }
}
