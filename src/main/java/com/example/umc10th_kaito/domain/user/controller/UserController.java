package com.example.umc10th_kaito.domain.user.controller;

import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.service.UserService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import com.example.umc10th_kaito.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody @Valid UserReqDTO.Register request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, userService.register(request));
    }

    // 로그인 → JWT Access Token 발급
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody @Valid UserReqDTO.Login request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.login(request));
    }

    @PostMapping("/register/preferences")
    public ApiResponse<?> registerPreferences(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserReqDTO.Preferences request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, null);
    }

    // 마이페이지 v1 - userId 파라미터 방식 (기존)
    @GetMapping("/mypage")
    public ApiResponse<?> getMyPage(@RequestParam Long userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.getMyPage(null));
    }

    // 마이페이지 v2 - JWT 토큰으로 인증된 사용자 정보 조회 (개선)
    @GetMapping("/v2/mypage")
    public ApiResponse<?> getMyPageV2(@AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.getMyPage(authUser));
    }
}
