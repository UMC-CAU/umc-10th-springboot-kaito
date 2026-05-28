package com.example.umc10th_kaito.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class UserResDTO {

    @Getter
    @Builder
    @JsonPropertyOrder({"userId", "email", "createdAt"})
    public static class RegisterResult {
        private Long userId;
        private String email;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @JsonPropertyOrder({"accessToken"})
    public static class LoginResult {
        private String accessToken;
    }

    @Getter
    @Builder
    @JsonPropertyOrder({"userId", "foodCategories"})
    public static class PreferencesResult {
        private Long userId;
        private List<String> foodCategories;
    }

    @Getter
    @Builder
    @JsonPropertyOrder({"userId", "name", "email", "phoneNumber", "totalPoint"})
    public static class MyPageResult {
        private Long userId;
        private String name;
        private String email;
        private String phoneNumber;
        private Integer totalPoint;
    }
}
