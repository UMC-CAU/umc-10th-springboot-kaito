package com.example.umc10th_kaito.domain.user.controller;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.service.UserService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import com.example.umc10th_kaito.global.security.entity.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // POST /auth/login - 로그인 후 JWT 토큰 발급
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid UserReqDTO.Login request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.login(request));
    }

    // POST /auth/register/preferences (TODO: 선호 음식 저장 미구현)
    @PostMapping("/register/preferences")
    public ApiResponse<?> registerPreferences(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserReqDTO.Preferences request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, null);
    }

    // GET /auth/v2/mypage - JWT 토큰으로 인증된 사용자 정보 조회
    @GetMapping("/v2/mypage")
    public ApiResponse<?> getMyPageV2(@AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.getMyPage(authUser));
    }
     
}
