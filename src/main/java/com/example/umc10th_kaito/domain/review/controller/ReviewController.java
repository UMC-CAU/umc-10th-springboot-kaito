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
