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
