package com.example.umc10th_kaito.domain.review.controller;
import com.example.umc10th_kaito.domain.review.dto.ReviewReqDTO;
import com.example.umc10th_kaito.domain.review.service.ReviewService;
import com.example.umc10th_kaito.global.apiPayload.ApiResponse;
import com.example.umc10th_kaito.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/missions")
public class ReviewController {
    private final ReviewService reviewService;
    // POST /missions/{userMissionId}/reviews
    @PostMapping("/{missionId}/reviews")
    public ApiResponse<?> createReview(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long missionId,
            @ModelAttribute ReviewReqDTO.CreateReview request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED,
                reviewService.createReview(missionId, request));
    }
}
