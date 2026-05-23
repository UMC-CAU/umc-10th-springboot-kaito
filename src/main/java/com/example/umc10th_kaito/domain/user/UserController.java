package com.example.umc10th_kaito.domain.user;

import com.example.umc10th_kaito.global.ApiResponse;
import com.example.umc10th_kaito.global.GeneralSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입: POST /auth/register
     * @Valid → UserReqDTO.Register 검증 → 실패 시 MethodArgumentNotValidException
     */
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody @Valid UserReqDTO.Register request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, userService.register(request));
    }

    @PostMapping("/register/preferences")
    public ApiResponse<?> registerPreferences(
            @RequestHeader("Authorization") String authorization,
            @RequestBody UserReqDTO.Preferences request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED, null);
    }

    @GetMapping("/mypage")
    public ApiResponse<?> getMyPage(@RequestParam Long userId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, userService.getMyPage(userId));
    }
}
