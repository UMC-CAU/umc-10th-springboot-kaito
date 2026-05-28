package com.example.umc10th_kaito.domain.user.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;
public class UserReqDTO {
    // POST /auth/register - 1단계 기본 정보
    @Getter
    public static class Register {

        @NotBlank(message = "이름은 필수입니다.")
        private String name;

        private String gender;

        private LocalDate birthDate;

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;

        private String address;   // 선택
    }
    // POST /auth/register/preferences - 2단계 선호 음식
    @Getter
    public static class Preferences {
        private List<String> foodCategories;
    }
}
