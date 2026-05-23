package com.example.umc10th_kaito.domain.review;

import com.example.umc10th_kaito.global.ApiResponse;
import com.example.umc10th_kaito.global.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /** 리뷰 작성: POST /missions/{userMissionId}/reviews */
    @PostMapping("/missions/{userMissionId}/reviews")
    public ApiResponse<?> createReview(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long userMissionId,
            @ModelAttribute ReviewReqDTO.CreateReview request) {
        return ApiResponse.onSuccess(GeneralSuccessCode.CREATED,
                reviewService.createReview(userMissionId, request));
    }

    /**
     * 내가 생성한 리뷰 목록: GET /reviews
     *
     * @param userId   조회 대상 유저 ID
     * @param pageSize 페이지 크기 (기본 10)
     * @param cursor   첫 요청 시 "-1", 이후 응답의 nextCursor 값 사용
     * @param query    정렬 기준: "id"(최신순) | "score"(별점순)
     */
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
