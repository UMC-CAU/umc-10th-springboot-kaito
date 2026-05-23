package com.example.umc10th_kaito.domain.review.dto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public class ReviewResDTO {
    @Getter @Builder
    public static class CreateReviewResult {
        private Long reviewId;
        private Long missionId;
        private Float rating;
        private String body;
        private List<String> imageUrls;
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    public static class ReviewItem {
        private Long reviewId;
        private String storeName;
        private Float score;
        private String body;
        private LocalDateTime createdAt;
    }

    @Getter @Builder
    public static class CursorPageResponse<T> {
        private List<T> data;
        private Boolean hasNext; // 다음 데이터 있는지
        private String nextCursor; // 다음 요청 시 쓸 커서값
        private Integer pageSize; // 가져온 개수
    }
}
