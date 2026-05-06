package com.example.umc10th_kaito.domain.review.service;
import com.example.umc10th_kaito.domain.mission.enums.MissionErrorCode;
import com.example.umc10th_kaito.domain.review.converter.ReviewConverter;
import com.example.umc10th_kaito.domain.review.dto.ReviewReqDTO;
import com.example.umc10th_kaito.domain.review.dto.ReviewResDTO;
import com.example.umc10th_kaito.domain.review.entity.Review;
import com.example.umc10th_kaito.domain.review.enums.ReviewErrorCode;
import com.example.umc10th_kaito.domain.review.repository.ReviewRepository;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.repository.UserMissionRepository;
import com.example.umc10th_kaito.global.apiPayload.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserMissionRepository userMissionRepository;
    @Transactional
    public ReviewResDTO.CreateReviewResult createReview(Long userMissionId, ReviewReqDTO.CreateReview request) {
        UserMission userMission = userMissionRepository.findById(userMissionId)
                .orElseThrow(() -> new ProjectException(MissionErrorCode.USER_MISSION_NOT_FOUND));
        if (reviewRepository.existsByUserMission_Id(userMissionId)) {
            throw new ProjectException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        Review review = Review.builder()
                .user(userMission.getUser())
                .store(userMission.getMission().getStore())
                .userMission(userMission)
                .body(request.getBody())
                .score(request.getRating())
                .build();
        return ReviewConverter.toCreateReviewResult(reviewRepository.save(review));
    }
}
