package com.example.umc10th_kaito.domain.user.dto;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;
public class UserReqDTO {
    // POST /auth/register - 1단계 기본 정보
    @Getter
    public static class Register {
        private String name;
        private String gender;
        private LocalDate birthDate;
        private String email;
        private String password;
        private String address;   // 선택
    }
    // POST /auth/register/preferences - 2단계 선호 음식
    @Getter
    public static class Preferences {
        private List<String> foodCategories;
    }
}
