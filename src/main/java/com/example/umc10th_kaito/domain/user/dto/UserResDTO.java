package com.example.umc10th_kaito.domain.user.dto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
public class UserResDTO {
    @Getter @Builder
    public static class RegisterResult {
        private Long userId;
        private String email;
        private LocalDateTime createdAt;
    }
    @Getter @Builder
    public static class PreferencesResult {
        private Long userId;
        private List<String> foodCategories;
    }
}
