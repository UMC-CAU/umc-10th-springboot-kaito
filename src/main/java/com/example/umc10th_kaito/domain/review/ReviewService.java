package com.example.umc10th_kaito.domain.review;

import com.example.umc10th_kaito.domain.mission.MissionErrorCode;
import com.example.umc10th_kaito.domain.user.UserMission;
import com.example.umc10th_kaito.domain.user.UserMissionRepository;
import com.example.umc10th_kaito.global.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 내가 생성한 리뷰 목록 커서 기반 페이지네이션
     *
     * @param userId   조회 대상 유저 ID
     * @param pageSize 한 번에 가져올 개수
     * @param cursor   "-1" = 첫 페이지 / id쿼리: "123" / score쿼리: "4.5:123"
     * @param query    "id" | "score"
     *
     * 커서 구조:
     *   - id 쿼리   : cursor = "마지막 review id"
     *   - score 쿼리: cursor = "마지막 score:마지막 id"  (동점 처리를 위해 복합 커서 사용)
     */
    @Transactional(readOnly = true)
    public ReviewResDTO.CursorPageResponse<ReviewResDTO.ReviewItem> getMyReviews(
            Long userId, Integer pageSize, String cursor, String query) {

        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Slice<Review> reviewSlice;

        if ("-1".equals(cursor)) {
            // ── 첫 페이지: 커서 없이 조회 ──
            if ("id".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByIdDesc(userId, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                reviewSlice = reviewRepository.findByUser_IdOrderByScoreDescIdDesc(userId, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        } else {
            // ── 이후 페이지: 커서 값 파싱 후 조회 ──
            if ("id".equalsIgnoreCase(query)) {
                Long idCursor = Long.parseLong(cursor);
                reviewSlice = reviewRepository.findByUser_IdAndIdLessThan(userId, idCursor, pageRequest);
            } else if ("score".equalsIgnoreCase(query)) {
                // cursor = "4.5:123"
                String[] parts = cursor.split(":");
                Float scoreCursor = Float.parseFloat(parts[0]);
                Long idCursorForScore = Long.parseLong(parts[1]);
                reviewSlice = reviewRepository.findByUser_IdWithScoreCursor(
                        userId, scoreCursor, idCursorForScore, pageRequest);
            } else {
                throw new ProjectException(ReviewErrorCode.INVALID_QUERY);
            }
        }

        List<ReviewResDTO.ReviewItem> data = reviewSlice.getContent().stream()
                .map(ReviewConverter::toReviewItem)
                .collect(Collectors.toList());

        // 다음 커서 계산: 마지막 아이템 기준
        String nextCursor = null;
        if (reviewSlice.hasNext() && !reviewSlice.getContent().isEmpty()) {
            Review last = reviewSlice.getContent().get(reviewSlice.getContent().size() - 1);
            if ("id".equalsIgnoreCase(query)) {
                nextCursor = String.valueOf(last.getId());
            } else if ("score".equalsIgnoreCase(query)) {
                nextCursor = last.getScore() + ":" + last.getId();
            }
        }

        return ReviewResDTO.CursorPageResponse.<ReviewResDTO.ReviewItem>builder()
                .data(data)
                .hasNext(reviewSlice.hasNext())
                .nextCursor(nextCursor)
                .pageSize(reviewSlice.getNumberOfElements())
                .build();
    }
}
