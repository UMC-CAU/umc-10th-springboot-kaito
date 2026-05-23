package com.example.umc10th_kaito.domain.review.repository;
import com.example.umc10th_kaito.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserMission_Id(Long userMissionId);

    // id 기준 첫 페이지
    Slice<Review> findByUser_IdOrderByIdDesc(Long userId, Pageable pageable);

    // id 기준 이후 페이지
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.id < :cursor ORDER BY r.id DESC")
    Slice<Review> findByUser_IdAndIdLessThan(
                @Param("userId") Long userId,
                @Param("cursor") Long cursor,
                Pageable pageable);

    // score 기준 첫 페이지
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdOrderByScoreDescIdDesc(
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.user.id = :userId " +
            "AND (r.score < :score OR (r.score = :score AND r.id < :id)) " +
            "ORDER BY r.score DESC, r.id DESC")
    Slice<Review> findByUser_IdWithScoreCursor(
            @Param("userId") Long userId,
            @Param("score") Float score,
            @Param("id") Long id,
            Pageable pageable);
 }

