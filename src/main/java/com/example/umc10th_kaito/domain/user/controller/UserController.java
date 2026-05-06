package com.example.umc10th_kaito.domain.user.controller;
import com.example.umc10th_kaito.domain.user.dto.UserReqDTO;
import com.example.umc10th_kaito.domain.user.service.UserService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    // POST /auth/register
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody UserReqDTO.Register request) {
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
