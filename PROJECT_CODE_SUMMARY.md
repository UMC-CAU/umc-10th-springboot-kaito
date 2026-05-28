# UMC 10th SpringBoot Kaito - 완전한 프로젝트 코드

이 파일에는 `umc10th_kaito` 프로젝트의 모든 Java 파일 코드가 포함되어 있습니다.

---

## 목차

1. **루트 레벨 파일들**
2. **Global 패키지 (글로벌 설정 및 예외 처리)**
3. **Domain 패키지 (각 도메인별)**
   - Common (공통)
   - User (사용자)
   - Mission (미션)
   - Home (홈)
   - Review (리뷰)
   - Store (상점)

---

## 1️⃣ 루트 레벨 파일들

### Umc10thKaitoApplication.java
```java
package com.example.umc10th_kaito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@SpringBootApplication
@EnableJpaAuditing
public class Umc10thKaitoApplication {
    public static void main(String[] args) {
        SpringApplication.run(Umc10thKaitoApplication.class, args);
    }
}
```

### Week5.java
```java
// week5 작업 시작
```

---

## 2️⃣ Global 패키지 (글로벌 설정 및 예외 처리)

### 📁 global/apiPayload/

#### ApiResponse.java
```java
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
```

### 📁 global/apiPayload/code/

#### BaseErrorCode.java
```java
package com.example.umc10th_kaito.global.apiPayload.code;
import org.springframework.http.HttpStatus;
public interface BaseErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
```

#### BaseSuccessCode.java
```java
package com.example.umc10th_kaito.global.apiPayload.code;
import org.springframework.http.HttpStatus;
public interface BaseSuccessCode {
    HttpStatus getStatus();
    String getCode();
    String getMessage();
}
```

#### GeneralErrorCode.java
```java
package com.example.umc10th_kaito.global.apiPayload.code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400_1", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401_1", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403_1", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404_1", "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500_1", "서버 내부 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

#### GeneralSuccessCode.java
```java
package com.example.umc10th_kaito.global.apiPayload.code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {
    OK(HttpStatus.OK, "COMMON200_1", "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201_1", "리소스가 성공적으로 생성되었습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

### 📁 global/apiPayload/exception/

#### ProjectException.java
```java
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
```

#### GeneralExceptionAdvice.java
```java
package com.example.umc10th_kaito.global.apiPayload.exception;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 스프링 전체에서 발생하는 Exception을 이 클래스가 잡겠다는 선언
public class GeneralExceptionAdvice {
    @ExceptionHandler(ProjectException.class) // ProjectException 타입의 모든 Exception을 낚아챔.
    public ResponseEntity<ApiResponse<Void>> handleProjectException(ProjectException e) { // 낚아챈 예외객체(e)를 인자로 받겠다. 이 안에 에러코드나 메세지,에러코드가 있음.
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        BaseErrorCode code = GeneralErrorCode.BAD_REQUEST;
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.onFailure(code, errors)); // 400 + 어떤 필드가 왜 실패했는지 응답
    }
}
```

### 📁 global/config/

#### SwaggerConfig.java
```java
package com.example.umc10th_kaito.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swagger() {
        Info info = new Info().title("UMC10th").description("10기 Swagger").version("0.0.1");

        // JWT 토큰 헤더 방식

        String securityScheme = "JWT TOKEN";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityScheme);

        Components components = new Components()
                .addSecuritySchemes(securityScheme, new SecurityScheme()
                        .name(securityScheme)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("/"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
```

---

## 3️⃣ Domain 패키지 (각 도메인별)

### 📁 domain/common/

#### BaseEntity.java
```java
package com.example.umc10th_kaito.domain.common;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
```

---

### 📁 domain/user/

#### enums/SocialType.java
```java
package com.example.umc10th_kaito.domain.user.enums;
public enum SocialType { KAKAO, NAVER, GOOGLE }
```

#### enums/MemberStatus.java
```java
package com.example.umc10th_kaito.domain.user.enums;
public enum MemberStatus { ACTIVE, INACTIVE, BANNED }
```

#### enums/Gender.java
```java
package com.example.umc10th_kaito.domain.user.enums;
public enum Gender { MALE, FEMALE, NONE }
```

#### enums/MissionStatus.java
```java
package com.example.umc10th_kaito.domain.user.enums;
public enum MissionStatus { CHALLENGING, COMPLETED, PENDING_APPROVAL }
```

#### enums/UserErrorCode.java
```java
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
```

#### entity/User.java
```java
package com.example.umc10th_kaito.domain.user.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.Gender;
import com.example.umc10th_kaito.domain.user.enums.MemberStatus;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false, length = 20)
    private SocialType socialType;

    @Column(name = "social_uid", unique = true, nullable = false)
    private String socialUid;

    @Column(nullable = false, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Gender gender = Gender.NONE;

    @Column(name = "birth_rate")
    private LocalDate birthRate;

    @Column(length = 255)
    private String address;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "is_phone_verified", nullable = false)
    @Builder.Default
    private Boolean isPhoneVerified = false;

    @Column(name = "total_point", nullable = false)
    @Builder.Default
    private Integer totalPoint = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberStatus status = MemberStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserMission> userMissionList = new ArrayList<>();
}
```

#### entity/mapping/UserMission.java
```java
package com.example.umc10th_kaito.domain.user.entity.mapping;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_mission")
public class UserMission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private MissionStatus status = MissionStatus.CHALLENGING;

    @Column(name = "verify_num", length = 10)
    private String verifyNum;
    public void updateStatus(MissionStatus status) {
        this.status = status;
    }
}
```

#### dto/UserReqDTO.java
```java
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
```

#### dto/UserResDTO.java
```java
package com.example.umc10th_kaito.domain.user.dto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
public class UserResDTO {
    @Getter @Builder
    @JsonPropertyOrder({"userId", "email", "createdAt"})
    public static class RegisterResult {
        private Long userId;
        private String email;
        private LocalDateTime createdAt;
    }
    @Getter @Builder
    @JsonPropertyOrder({"userId", "foodCategories"})
    public static class PreferencesResult {
        private Long userId;
        private List<String> foodCategories;
    }
    @Getter @Builder
    @JsonPropertyOrder({"userId", "name", "email", "phoneNumber", "totalPoint"})
    public static class MyPageResult {
        private Long userId;
        private String name;
        private String email;
        private String phoneNumber;
        private Integer totalPoint;
    }
}
```

#### converter/UserConverter.java
```java
package com.example.umc10th_kaito.domain.user.converter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.SocialType;
import java.time.LocalDateTime;
public class UserConverter {
    public static User toUser(UserReqDTO.Register request) { // 1. DTO를 인자로 받아서 엔티티로 바꾸는 곳 (저장하기 위해)
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .socialType(SocialType.KAKAO) // 임시 (소셜 로그인 미구현)
                .socialUid(request.getEmail()) // 임시
                .address(request.getAddress())
                .build();
    }
    public static UserResDTO.RegisterResult toRegisterResult(User user) { // 2. 엔티티를 인자로 받아서 DTO로 바꾸는 곳 (클라이언트에게 응답하기 위해)
        return UserResDTO.RegisterResult.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static UserResDTO.MyPageResult toMyPageResult(User user) {
        return UserResDTO.MyPageResult.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .totalPoint(user.getTotalPoint())
                .build();
    }
}
```

#### repository/UserRepository.java
```java
package com.example.umc10th_kaito.domain.user.repository;
import com.example.umc10th_kaito.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

#### repository/UserMissionRepository.java
```java
package com.example.umc10th_kaito.domain.user.repository;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    // 유저의 미션 목록 (상태별, 페이징)
    @Query("SELECT um FROM UserMission um WHERE um.user.id = :userId AND um.status = :status")
    Page<UserMission> findByUserIdAndStatus(@Param("userId") Long userId,
                                            @Param("status") MissionStatus status,
                                            Pageable pageable);
    // 진행중 미션 총 포인트 합산
    @Query("SELECT COALESCE(SUM(um.mission.rewardValue), 0) FROM UserMission um WHERE um.user.id = :userId AND um.status = :status")
    Integer sumRewardByUserIdAndStatus(@Param("userId") Long userId, @Param("status") MissionStatus status);
    long countByUser_Id(Long userId);
    long countByUser_IdAndStatus(Long userId, MissionStatus status);
}
```

#### service/UserService.java
```java
package com.example.umc10th_kaito.domain.user.service;
import com.example.umc10th_kaito.domain.user.converter.UserConverter;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.dto.UserResDTO;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public UserResDTO.RegisterResult register(UserReqDTO.Register request) { // JSON 데이터를 DTO 객체로 받는다.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ProjectException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User user = UserConverter.toUser(request); // 받은 DTO객체를 엔티티로 변환한다.
        return UserConverter.toRegisterResult(userRepository.save(user));
    }
    @Transactional(readOnly = true)
    public UserResDTO.MyPageResult getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND)); // 이 줄이 실행되는 순간, 메모리에 ProjectException 객체가 생성되고, 그 객체는 UserErrorCode.USER_NOT_FOUND를 포함하게 된다. 그리고 나서 throw 키워드에 의해 이 예외가 즉시 던져진다. 이 예외는 호출 스택을 따라 올라가면서 적절한 예외 처리기에 의해 처리될 때까지 전파된다.
        return UserConverter.toMyPageResult(user);
    }
}
```

#### controller/UserController.java
```java
package com.example.umc10th_kaito.domain.user.controller;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.service.UserService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController // 외부의 클라이언트로부터 HTTP 요청을 받는데, 이때의 데이터의 형태는 @RequestBody(JSON), @RequestParam(쿼리 스트링), @PathVariable(인증 토큰)등의 형태로 받는다. 그리고 이 컨트롤러에서 반환하는 데이터는 JSON 형태로 외부에 전달된다.
                // 이때 우리가 약속한 ApiResponse라는 봉투에 데이터를 담아서 JSON 형태로 전달하는 것이다.
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    // POST /auth/register
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody @Valid UserReqDTO.Register request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, userService.register(request));
    }
    // POST /auth/register/preferences (TODO: 선호 음식 저장 미구현)
    @PostMapping("/register/preferences")
    public ApiResponse<?> registerPreferences(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserReqDTO.Preferences request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, null);
    }
    // GET /auth/mypage?userId=1
    @GetMapping("/mypage")
    public ApiResponse<?> getMyPage(@RequestParam Long userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.getMyPage(userId));
    }
}
```

---

### 📁 domain/mission/

#### enums/MissionActiveStatus.java
```java
package com.example.umc10th_kaito.domain.mission.enums;
public enum MissionActiveStatus { ACTIVE, INACTIVE }
```

#### enums/MissionErrorCode.java
```java
package com.example.umc10th_kaito.domain.mission.enums;
import com.example.umc10th_kaito.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum MissionErrorCode implements BaseErrorCode {
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_1", "해당 미션을 찾을 수 없습니다."),
    USER_MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION404_2", "진행중인 미션을 찾을 수 없습니다."),
    MISSION_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "MISSION400_1", "이미 완료된 미션입니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
```

#### enums/RewardType.java
```java
package com.example.umc10th_kaito.domain.mission.enums;
public enum RewardType { POINT, RATE }
```

#### entity/Mission.java
```java
package com.example.umc10th_kaito.domain.mission.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import com.example.umc10th_kaito.domain.mission.enums.RewardType;
import com.example.umc10th_kaito.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mission")
public class Mission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // Mission과 Store은 N:1관계, DB구조상 store_id라는 연결고리(외래키)는 N쪽 테이블에 생성.

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false, length = 10)
    @Builder.Default
    private RewardType rewardType = RewardType.POINT;

    @Column(name = "reward_value", nullable = false)
    private Integer rewardValue;

    @Column(nullable = false)
    private Integer deadline;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MissionActiveStatus status = MissionActiveStatus.ACTIVE;
}
```

#### dto/MissionResDTO.java
```java
package com.example.umc10th_kaito.domain.mission.dto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

public class MissionResDTO {
    @Getter @Builder
    public static class MissionItem {
        private Long missionId;
        private String storeName;
        private String storeCategory;
        private String condition;
        private int rewardPoint;
        private String rewardType; // "POINT" 또는 "RATE"
        private Integer dDay;
        private String status;
        private LocalDateTime completedAt;
    }
    @Getter @Builder
    public static class SuccessResult {
        private Long missionId;
        private String status;
        private LocalDateTime requestedAt;
    }

    @Getter @Builder
    public static class OffsetPageResponse<T> {
        private List<T> data;          // 실제 데이터 (content)
        private int pageNumber;        // 현재 페이지 번호
        private int pageSize;          // 페이지 크기
        private long totalElements;    // 전체 데이터 개수
        private int totalPages;        // 전체 페이지 수
        private boolean first;         // 첫 페이지인지
        private boolean last;          // 마지막 페이지인지
    }
}
```

#### converter/MissionConverter.java
```java
package com.example.umc10th_kaito.domain.mission.converter;
import com.example.umc10th_kaito.domain.mission.dto.MissionResDTO;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import org.springframework.data.domain.Page;

public class MissionConverter {
    public static MissionResDTO.MissionItem toMissionItem(UserMission userMission) {
        return MissionResDTO.MissionItem.builder()
                .missionId(userMission.getId())
                .storeName(userMission.getMission().getStore().getName())
                .storeCategory(userMission.getMission().getStore().getCategory())
                .condition(userMission.getMission().getContent())
                .rewardPoint(userMission.getMission().getRewardValue())
                .rewardType(userMission.getMission().getRewardType().name())  // 추가
                .dDay(userMission.getMission().getDeadline())
                .status(userMission.getStatus().name())
                .completedAt(userMission.getStatus() == MissionStatus.COMPLETED
                        ? userMission.getUpdatedAt() : null)
                .build();
    }
    public static MissionResDTO.SuccessResult toSuccessResult(UserMission userMission) {
        return MissionResDTO.SuccessResult.builder()
                .missionId(userMission.getId())
                .status(userMission.getStatus().name())
                .requestedAt(userMission.getUpdatedAt())
                .build();
    }

    public static <T> MissionResDTO.OffsetPageResponse<T> toOffsetPageResponse(Page<T> page) {
        return MissionResDTO.OffsetPageResponse.<T>builder()
                .data(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
```

#### repository/MissionRepository.java
```java
package com.example.umc10th_kaito.domain.mission.repository;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // 홈 화면: 선택된 지역에서 도전 가능한 미션 목록 (페이징)
    @Query("SELECT m FROM Mission m JOIN m.store s WHERE s.area.id = :areaId AND m.status = :status") // JPQL
    Page<Mission> findByAreaIdAndStatus(@Param("areaId") Long areaId,
                                        @Param("status") MissionActiveStatus status,
                                        Pageable pageable);
}
```

#### service/MissionService.java
```java
package com.example.umc10th_kaito.domain.mission.service;
import com.example.umc10th_kaito.domain.mission.converter.MissionConverter;
import com.example.umc10th_kaito.domain.mission.dto.MissionResDTO;
import com.example.umc10th_kaito.domain.mission.enums.MissionErrorCode;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class MissionService {
    private final UserMissionRepository userMissionRepository;
    // 미션 목록 조회 (진행중 / 진행완료, 페이징)
    @Transactional(readOnly = true)
    public MissionResDTO.OffsetPageResponse<MissionResDTO.MissionItem> getMissions(Long userId, String status, int page, int size) {
        MissionStatus missionStatus;
        try {
            missionStatus = MissionStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ProjectException(MissionErrorCode.MISSION_NOT_FOUND);
        }
        Page<MissionResDTO.MissionItem> missionPage = userMissionRepository
                .findByUserIdAndStatus(userId, missionStatus, PageRequest.of(page, size))
                .map(MissionConverter::toMissionItem); // 클래스명::메서드명
                // 이 변환과정을 통해 상자 안의 알맹이가 Entity에서 DTO로 변경 후 좌변의 Page 상자에 담긴다.

        return MissionConverter.toOffsetPageResponse(missionPage);
    }
    // 미션 성공 누르기
    @Transactional
    public MissionResDTO.SuccessResult completeMission(Long userMissionId) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new ProjectException(MissionErrorCode.USER_MISSION_NOT_FOUND));
        if (userMission.getStatus() == MissionStatus.COMPLETED) {
            throw new ProjectException(MissionErrorCode.MISSION_ALREADY_COMPLETED);
        }
        userMission.updateStatus(MissionStatus.PENDING_APPROVAL);
        return MissionConverter.toSuccessResult(userMission);
    }
}
```

#### controller/MissionController.java
```java
package com.example.umc10th_kaito.domain.mission.controller;
import com.example.umc10th_kaito.domain.mission.service.MissionService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class MissionController {
    private final MissionService missionService;
    // GET /missions?status=CHALLENGING&userId=1&page=0&size=20
    @GetMapping
    public ApiResponse<?> getMissions(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String status,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK,
                missionService.getMissions(userId, status, page, size));
    }
    // POST /missions/{userMissionId}/success
    @PostMapping("/{missionId}/success")
    public ApiResponse<?> completeMission(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long missionId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, missionService.completeMission(missionId));
    }
}
```

---

### 📁 domain/home/

#### dto/HomeResDTO.java
```java
package com.example.umc10th_kaito.domain.home.dto;
import lombok.Builder;
import lombok.Getter;
public class HomeResDTO {
    @Getter @Builder
    public static class Summary {
        private int completedCount;
        private int totalCount;
        private int rewardPoint;
        private int currentPoint;
        private String nickname;
        private String location;
    }
    @Getter @Builder
    public static class MissionItem {
        private Long missionId;
        private String storeName;
        private String storeCategory;
        private String condition;
        private int rewardPoint;
        private String rewardType; // "POINT" 또는 "RATE"
        private Integer dDay;
        private String status;
    }
}
```

#### service/HomeService.java
```java
package com.example.umc10th_kaito.domain.home.service;
import com.example.umc10th_kaito.domain.home.dto.HomeResDTO;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import com.example.umc10th_kaito.domain.mission.repository.MissionRepository;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import com.example.umc10th_kaito.domain.user.enums.UserErrorCode;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.domain.user.repository.UserRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    // 홈 화면 상단 현황 조회
    @Transactional(readOnly = true)
    public HomeResDTO.Summary getHomeSummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ProjectException(UserErrorCode.USER_NOT_FOUND));
        long totalCount = userMissionRepository.countByUser_Id(userId);
        long completedCount = userMissionRepository.countByUser_IdAndStatus(userId, MissionStatus.COMPLETED);
        Integer rewardPoint = userMissionRepository.sumRewardByUserIdAndStatus(userId, MissionStatus.CHALLENGING);
        return HomeResDTO.Summary.builder()
                .completedCount((int) completedCount)
                .totalCount((int) totalCount)
                .rewardPoint(rewardPoint != null ? rewardPoint : 0)
                .currentPoint(user.getTotalPoint())
                .nickname(user.getName())
                .location(user.getAddress() != null ? user.getAddress() : "")
                .build();
    }
    // 홈 화면 하단 지역 미션 목록 (페이징)
    @Transactional(readOnly = true)
    public Page<HomeResDTO.MissionItem> getHomeMissions(Long areaId, int page, int size) {
        return missionRepository
                .findByAreaIdAndStatus(areaId, MissionActiveStatus.ACTIVE, PageRequest.of(page, size))
                .map(this::toMissionItem);
    }
    private HomeResDTO.MissionItem toMissionItem(Mission m) {
        return HomeResDTO.MissionItem.builder()
                .missionId(m.getId())
                .storeName(m.getStore().getName())
                .storeCategory(m.getStore().getCategory())
                .condition(m.getContent())
                .rewardPoint(m.getRewardValue())
                .rewardType(m.getRewardType().name())  // 추가
                .dDay(m.getDeadline())
                .status(m.getStatus().name())
                .build();
    }
}
```

#### controller/HomeController.java
```java
package com.example.umc10th_kaito.domain.home.controller;
import com.example.umc10th_kaito.domain.home.service.HomeService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;
    // GET /home/summary?userId=1
    @GetMapping("/summary")
    public ApiResponse<?> getHomeSummary(
            @RequestHeader("Authorization") String authorization,
            @RequestParam Long userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, homeService.getHomeSummary(userId));
    }
    // GET /home/missions?areaId=1&page=0&size=10
    @GetMapping("/missions")
    public ApiResponse<?> getHomeMissions(
            @RequestHeader("Authorization") String authorization,
            @RequestParam Long areaId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, homeService.getHomeMissions(areaId, page, size));
    }
}
```

---

### 📁 domain/review/

#### enums/ReviewErrorCode.java
```java
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
```

#### entity/Review.java
```java
package com.example.umc10th_kaito.domain.review.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import com.example.umc10th_kaito.domain.store.entity.Store;
import com.example.umc10th_kaito.domain.user.entity.User;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_mission_id", nullable = false, unique = true)
    private UserMission userMission;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private Float score;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReviewImage> reviewImageList = new ArrayList<>();
}
```

#### entity/ReviewImage.java
```java
package com.example.umc10th_kaito.domain.review.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_image")
public class ReviewImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
```

#### dto/ReviewReqDTO.java
```java
package com.example.umc10th_kaito.domain.review.dto;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
public class ReviewReqDTO {
    // POST /missions/{missionId}/reviews - multipart/form-data
    @Getter
    public static class CreateReview {
        private Float rating;               // 별점 (필수, 1.0~5.0)
        private String body;                // 리뷰 내용 (필수, 최대 500자)
        private List<MultipartFile> images; // 이미지 (선택)
    }
}
```

#### dto/ReviewResDTO.java
```java
package com.example.umc10th_kaito.domain.review.dto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public class ReviewResDTO {
    @Getter @Builder
    public static class CreateReviewResult {
        private Long reviewId;
        private Long missionId;
        private Float rating;
        private String body;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    public static class ReviewItem {
        private Long reviewId;
        private String storeName;
        private Float score;
        private String body;
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    public static class CursorPageResponse<T> {
        private List<T> data;
        private Boolean hasNext; // 다음 데이터 있는지
        private String nextCursor; // 다음 요청 시 쓸 커서값
        private Integer pageSize; // 가져온 개수
    }
}
```

#### converter/ReviewConverter.java
```java
package com.example.umc10th_kaito.domain.review.converter;
import com.example.umc10th_kaito.domain.review.dto.ReviewResDTO;
import com.example.umc10th_kaito.domain.review.entity.Review;
import java.util.Collections;
public class ReviewConverter {
    public static ReviewResDTO.CreateReviewResult toCreateReviewResult(Review review) {
        return ReviewResDTO.CreateReviewResult.builder()
                .reviewId(review.getId())
                .missionId(review.getUserMission().getId())
                .rating(review.getScore())
                .body(review.getBody())
                .imageUrls(Collections.emptyList()) // 이미지는 추후 구현
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ReviewResDTO.ReviewItem toReviewItem(Review review) {
        return ReviewResDTO.ReviewItem.builder()
                .reviewId(review.getId())
                .storeName(review.getStore().getName())
                .score(review.getScore())
                .body(review.getBody())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
```

#### repository/ReviewRepository.java
```java
package com.example.umc10th_kaito.domain.review.repository;
import com.example.umc10th_kaito.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserMission_Id(Long userMissionId);

    // id 기준 첫 페이지
    Slice<Review> findByUser_IdOrderByIdDesc(Long userId, Pageable pageable);

    // id 기준 이후 페이지
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.id < :cursor ORDER BY r.id DESC")
    Slice<Review> findByUser_IdAndIdLessThan(
                @Param("userId") Long userId,
                @Param("cursor") Long cursor,
                Pageable pageable);

    // score 기준 첫 페이지
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdOrderByScoreDescIdDesc(
            @Param("userId") Long userId,
            Pageable pageable);

    // score 기준 이후 페이지
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId " +
            "AND (r.score < :score OR (r.score = :score AND r.id < :id)) " +
            "ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdWithScoreCursor(
            @Param("userId") Long userId,
            @Param("score") Float score,
            @Param("id") Long id,
            Pageable pageable);
 }
```

#### service/ReviewService.java
```java
package com.example.umc10th_kaito.domain.review.service;
import com.example.umc10th_kaito.domain.mission.enums.MissionErrorCode;
import com.example.umc10th_kaito.domain.review.converter.ReviewConverter;
import com.example.umc10th_kaito.domain.review.dto.ReviewReqDTO;
import com.example.umc10th_kaito.domain.review.dto.ReviewResDTO;
import com.example.umc10th_kaito.domain.review.entity.Review;
import com.example.umc10th_kaito.domain.review.enums.ReviewErrorCode;
import com.example.umc10th_kaito.domain.review.repository.ReviewRepository;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserMissionRepository userMissionRepository;

    // 리뷰 작성
    @Transactional
    public ReviewResDTO.CreateReviewResult createReview(Long userMissionId, ReviewReqDTO.CreateReview request) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new ProjectException(MissionErrorCode.USER_MISSION_NOT_FOUND));
        if (reviewRepository.existsByUserMission_Id(userMissionId)) {
            throw new ProjectException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        Review review = Review.builder()
                .user(userMission.getUser())
                .store(userMission.getMission().getStore())
                .userMission(userMission)
                .body(request.getBody())
                .score(request.getRating())
                .build();
        return ReviewConverter.toCreateReviewResult(reviewRepository.save(review));
    }

    // 리뷰 목록 조회 (페이징, 커서 기반)
    @Transactional(readOnly = true) // DB를 읽기만 하는 메서드
    public ReviewResDTO.CursorPageResponse<ReviewResDTO.ReviewItem> getMyReviews(
            Long userId, Integer pageSize, String cursor, String query) {

        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Slice<Review> reviewSlice;

        if ("-1".equals(cursor)) {
            // 첫 페이지: 커서 없이 조회
            if ("id".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByIdDesc(userId, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByScoreDescIdDesc(userId, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        } else {
            // 이후 페이지: 커서 값으로 조회
            if ("id".equalsIgnoreCase(query)) {
                Long idCursor = Long.parseLong(cursor);
                reviewSlice = reviewRepository.findByUser_IdAndIdLessThan(userId, idCursor, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                // cursor = "4.5:123" 형태
                String[] parts = cursor.split(":");
                Float scoreCursor = Float.parseFloat(parts[0]);
                Long idCursor = Long.parseLong(parts[1]);
                reviewSlice = reviewRepository.findByUser_IdWithScoreCursor(userId, scoreCursor, idCursor, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        }

        // 다음 커서 계산
        String nextCursor = null;
        if (reviewSlice.hasNext()) {
            Review last = reviewSlice.getContent().get(reviewSlice.getContent().size() - 1);
            if ("id".equalsIgnoreCase(query)) {
                nextCursor = String.valueOf(last.getId());
            } else if ("score".equalsIgnoreCase(query)) {
                nextCursor = last.getScore() + ":" + last.getId();
            }
        }

        return ReviewResDTO.CursorPageResponse.<ReviewResDTO.ReviewItem>builder()
                .data(reviewSlice.getContent().stream()
                        .map(ReviewConverter::toReviewItem)
                        .collect(Collectors.toList()))
                .hasNext(reviewSlice.hasNext())
                .nextCursor(nextCursor)
                .pageSize(reviewSlice.getNumberOfElements())
                .build();
    }
}
```

#### controller/ReviewController.java
```java
package com.example.umc10th_kaito.domain.review.controller;
import com.example.umc10th_kaito.domain.review.dto.ReviewReqDTO;
import com.example.umc10th_kaito.domain.review.service.ReviewService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/missions/{userMissionId}/reviews")
    public ApiResponse<?> createReview(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long userMissionId,
            @ModelAttribute ReviewReqDTO.CreateReview request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED,
                reviewService.createReview(userMissionId, request));
    }

    @GetMapping("/reviews")
    public ApiResponse<?> getMyReviews(
            @RequestHeader("Authorization") String authorization,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "-1") String cursor,
            @RequestParam(defaultValue = "id") String query) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK,
                reviewService.getMyReviews(userId, pageSize, cursor, query));
    }
}
```

---

### 📁 domain/store/

#### entity/Store.java
```java
package com.example.umc10th_kaito.domain.store.entity;
import com.example.umc10th_kaito.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "FLOAT DEFAULT 0")
    private Float score;
}
```

#### entity/Area.java
```java
package com.example.umc10th_kaito.domain.store.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_name", nullable = false, length = 50)
    private String areaName;
}
```

#### repository/StoreRepository.java
```java
package com.example.umc10th_kaito.domain.store.repository;
import com.example.umc10th_kaito.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StoreRepository extends JpaRepository<Store, Long> {}
```

#### repository/AreaRepository.java
```java
package com.example.umc10th_kaito.domain.store.repository;
import com.example.umc10th_kaito.domain.store.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AreaRepository extends JpaRepository<Area, Long> {}
```

---

## 📋 프로젝트 구조 요약

### **API 엔드포인트 목록**

| HTTP | Endpoint | 설명 |
|------|----------|------|
| POST | `/auth/register` | 사용자 등록 |
| POST | `/auth/register/preferences` | 선호 음식 저장 (미구현) |
| GET | `/auth/mypage` | 내 정보 조회 |
| GET | `/missions` | 미션 목록 조회 |
| POST | `/missions/{missionId}/success` | 미션 완료 |
| GET | `/home/summary` | 홈 화면 상단 현황 |
| GET | `/home/missions` | 홈 화면 하단 미션 목록 |
| POST | `/missions/{userMissionId}/reviews` | 리뷰 작성 |
| GET | `/reviews` | 내 리뷰 목록 조회 |

### **주요 디자인 패턴**

1. **계층 구조**: Controller → Service → Repository → Entity
2. **DTO 변환**: Entity와 클라이언트 간 데이터 격리 (Converter 사용)
3. **예외 처리**: 커스텀 `ProjectException` 및 `@RestControllerAdvice`
4. **페이징**: Offset 기반 페이징 및 커서 기반 페이징
5. **트랜잭션**: `@Transactional` 및 읽기 전용 쿼리 최적화

---

이 파일에 프로젝트의 모든 Java 코드가 포함되어 있습니다. 이하 작업이나 수정이 필요하신 부분이 있으면 말씀해주세요!

