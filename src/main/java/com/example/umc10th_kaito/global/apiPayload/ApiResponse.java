package com.example.umc10th_kaito.global.apiPayload;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.example.umc10th_kaito.global.apiPayload.code.BaseSuccessCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"}) // 클라이언트에게 보여지는 JSON 필드 순서 지정
public class ApiResponse<T> { // T인 이유는 응답데이터(result) 자리에 다양한 타입이 올 수 있기 때문.
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private T result;
    public static <T> ApiResponse<T> onSuccess(BaseSuccessCode successCode, T result) {
        return new ApiResponse<>(true, successCode.getCode(), successCode.getMessage(), result);
    }
    public static <T> ApiResponse<T> onFailure(BaseErrorCode errorCode, T result) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), result);
    }
}
