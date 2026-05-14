package com.example.umc10th_kaito.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserMission_Id(Long userMissionId);

    // ── ID 순 (커서: review id) ──────────────────────────────────

    /** 첫 페이지: 커서 없이 최신순 */
    Slice<Review> findByUser_IdOrderByIdDesc(Long userId, Pageable pageable);

    /** 이후 페이지: cursor(id) 미만인 데이터만 */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.id < :cursor ORDER BY r.id DESC")
    Slice<Review> findByUser_IdAndIdLessThan(
            @Param("userId") Long userId,
            @Param("cursor") Long cursor,
            Pageable pageable);

    // ── 별점 순 (커서: score:id 복합) ────────────────────────────

    /** 첫 페이지: 커서 없이 별점 내림차순, 동점이면 id 내림차순 */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdOrderByScoreDescIdDesc(
            @Param("userId") Long userId,
            Pageable pageable);

    /**
     * 이후 페이지: (score < :score) 또는 (score = :score AND id < :id)
     * 1주차 커서 페이지네이션 쿼리와 동일한 방식
     */
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId " +
            "AND (r.score < :score OR (r.score = :score AND r.id < :id)) " +
            "ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdWithScoreCursor(
            @Param("userId") Long userId,
            @Param("score") Float score,
            @Param("id") Long id,
            Pageable pageable);
}
