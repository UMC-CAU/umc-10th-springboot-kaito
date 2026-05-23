package com.example.umc10th_kaito.domain.user.repository;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    // 유저의 미션 목록 (상태별, 페이징)
    @Query("SELECT um FROM UserMission um WHERE um.user.id = :userId AND um.status = :status")
    Page<UserMission> findByUserIdAndStatus(@Param("userId") Long userId,
                                            @Param("status") MissionStatus status,
                                            Pageable pageable);
    // 진행중 미션 총 포인트 합산
    @Query("SELECT COALESCE(SUM(um.mission.rewardValue), 0) FROM UserMission um WHERE um.user.id = :userId AND um.status = :status")
    Integer sumRewardByUserIdAndStatus(@Param("userId") Long userId, @Param("status") MissionStatus status);
    long countByUser_Id(Long userId);
    long countByUser_IdAndStatus(Long userId, MissionStatus status);
}
