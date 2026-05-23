package com.example.umc10th_kaito.domain.review;

import java.util.Collections;

public class ReviewConverter {

    public static ReviewResDTO.CreateReviewResult toCreateReviewResult(Review review) {
        return ReviewResDTO.CreateReviewResult.builder()
                .reviewId(review.getId())
                .missionId(review.getUserMission().getId())
                .rating(review.getScore())
                .body(review.getBody())
                .imageUrls(Collections.emptyList())
                .createdAt(review.getCreatedAt())
                .build();
    }

    /** Review 엔티티 → ReviewItem DTO (사진 제외) */
    public static ReviewResDTO.ReviewItem toReviewItem(Review review) {
        return ReviewResDTO.ReviewItem.builder()
                .reviewId(review.getId())
                .storeName(review.getStore().getName())
                .score(review.getScore())
                .body(review.getBody())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
