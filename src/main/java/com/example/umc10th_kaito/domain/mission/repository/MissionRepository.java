package com.example.umc10th_kaito.domain.mission.repository;
import com.example.umc10th_kaito.domain.mission.entity.Mission;
import com.example.umc10th_kaito.domain.mission.enums.MissionActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // 홈 화면: 선택된 지역에서 도전 가능한 미션 목록 (페이징)
    @Query("SELECT m FROM Mission m JOIN m.store s WHERE s.area.id = :areaId AND m.status = :status")
    Page<Mission> findByAreaIdAndStatus(@Param("areaId") Long areaId,
                                        @Param("status") MissionActiveStatus status,
                                        Pageable pageable);
}
