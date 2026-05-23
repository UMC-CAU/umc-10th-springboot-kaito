package com.example.umc10th_kaito.domain.review;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {

    @Getter
    @Builder
    public static class CreateReviewResult {
        private Long reviewId;
        private Long missionId;
        private Float rating;
        private String body;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
    }

    /** 리뷰 목록 조회 단일 아이템 (사진 필드 제외) */
    @Getter
    @Builder
    public static class ReviewItem {
        private Long reviewId;
        private String storeName;
        private Float score;
        private String body;
        private LocalDateTime createdAt;
    }

    /**
     * 커서 기반 페이지네이션 응답 DTO
     * hasNext: 다음 페이지 존재 여부
     * nextCursor: 다음 요청 시 cursor 파라미터 값
     *   - id 쿼리:    "123"          (마지막 리뷰 id)
     *   - score 쿼리: "4.5:123"      (마지막 리뷰 score:id)
     */
    @Getter
    @Builder
    public static class CursorPageResponse<T> {
        private List<T> data;
        private Boolean hasNext;
        private String nextCursor;
        private Integer pageSize;
    }
}
