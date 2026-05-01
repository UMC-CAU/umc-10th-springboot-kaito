package com.example.umc10th_kaito.domain.home.controller;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    // GET /home/summary - 홈 화면 현황 조회
    @GetMapping("/summary")
    public ApiResponse<?> getHomeSummary(
            @RequestHeader("Authorization") String authorization) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
    // GET /home/missions - 홈 화면 미션 목록
    @GetMapping("/missions")
    public ApiResponse<?> getHomeMissions(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
