package com.example.umc10th_kaito.domain.review.converter;
import com.example.umc10th_kaito.domain.review.dto.ReviewResDTO;
import com.example.umc10th_kaito.domain.review.entity.Review;
import java.util.Collections;
public class ReviewConverter {
    public static ReviewResDTO.CreateReviewResult toCreateReviewResult(Review review) {
        return ReviewResDTO.CreateReviewResult.builder()
                .reviewId(review.getId())
                .missionId(review.getUserMission().getId())
                .rating(review.getScore())
                .body(review.getBody())
                .imageUrls(Collections.emptyList()) // 이미지는 추후 구현
                .createdAt(review.getCreatedAt())
                .build();
    }

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
