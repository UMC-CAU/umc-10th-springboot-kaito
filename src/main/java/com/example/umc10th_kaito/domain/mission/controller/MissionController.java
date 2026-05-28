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
