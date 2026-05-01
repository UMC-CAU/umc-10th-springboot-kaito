package com.example.umc10th_kaito.domain.review.dto;
import lombok.Builder;
import lombok.Getter;
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
}
