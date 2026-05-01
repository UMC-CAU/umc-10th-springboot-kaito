package com.example.umc10th_kaito.domain.mission.controller;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class MissionController {
    // GET /missions?status=CHALLENGING - 진행중 미션 목록
    // GET /missions?status=COMPLETED   - 진행완료 미션 목록
    @GetMapping
    public ApiResponse<?> getMissions(
            @RequestHeader("Authorization") String authorization,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String sort) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
    // POST /missions/{missionId}/success - 미션 성공 누르기
    @PostMapping("/{missionId}/success")
    public ApiResponse<?> completeMission(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long missionId) {
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, null);
    }
}
