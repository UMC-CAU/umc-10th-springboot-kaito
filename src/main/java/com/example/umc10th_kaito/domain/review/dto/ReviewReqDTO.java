package com.example.umc10th_kaito.domain.review.dto;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
public class ReviewReqDTO {
    // POST /missions/{missionId}/reviews - multipart/form-data
    @Getter
    public static class CreateReview {
        private Float rating;               // 별점 (필수, 1.0~5.0)
        private String body;                // 리뷰 내용 (필수, 최대 500자)
        private List<MultipartFile> images; // 이미지 (선택)
    }
}
