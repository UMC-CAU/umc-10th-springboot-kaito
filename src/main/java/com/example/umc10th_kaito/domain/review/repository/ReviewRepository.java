package com.example.umc10th_kaito.domain.review.repository;
import com.example.umc10th_kaito.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserMission_Id(Long userMissionId);
}
