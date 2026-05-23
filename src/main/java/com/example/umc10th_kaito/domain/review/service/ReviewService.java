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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserMissionRepository userMissionRepository;

    // 리뷰 작성
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

    // 리뷰 목록 조회 (페이징, 커서 기반)
    @Transactional(readOnly = true)
    public ReviewResDTO.CursorPageResponse<ReviewResDTO.ReviewItem> getMyReviews(
            Long userId, Integer pageSize, String cursor, String query) {

        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Slice<Review> reviewSlice;

        if ("-1".equals(cursor)) {
            // 첫 페이지: 커서 없이 조회
            if ("id".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByIdDesc(userId, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByScoreDescIdDesc(userId, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        } else {
            // 이후 페이지: 커서 값으로 조회
            if ("id".equalsIgnoreCase(query)) {
                Long idCursor = Long.parseLong(cursor);
                reviewSlice = reviewRepository.findByUser_IdAndIdLessThan(userId, idCursor, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                // cursor = "4.5:123" 형태
                String[] parts = cursor.split(":");
                Float scoreCursor = Float.parseFloat(parts[0]);
                Long idCursor = Long.parseLong(parts[1]);
                reviewSlice = reviewRepository.findByUser_IdWithScoreCursor(userId, scoreCursor, idCursor, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        }

        // 다음 커서 계산
        String nextCursor = null;
        if (reviewSlice.hasNext()) {
            Review last = reviewSlice.getContent().get(reviewSlice.getContent().size() - 1);
            if ("id".equalsIgnoreCase(query)) {
                nextCursor = String.valueOf(last.getId());
            } else if ("score".equalsIgnoreCase(query)) {
                nextCursor = last.getScore() + ":" + last.getId();
            }
        }

        return ReviewResDTO.CursorPageResponse.<ReviewResDTO.ReviewItem>builder()
                .data(reviewSlice.getContent().stream()
                        .map(ReviewConverter::toReviewItem)
                        .collect(Collectors.toList()))
                .hasNext(reviewSlice.hasNext())
                .nextCursor(nextCursor)
                .pageSize(reviewSlice.getNumberOfElements())
                .build();
    }
}
