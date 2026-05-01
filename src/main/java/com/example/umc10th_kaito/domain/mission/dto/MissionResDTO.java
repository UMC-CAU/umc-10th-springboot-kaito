package com.example.umc10th_kaito.domain.mission.dto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
public class MissionResDTO {
    @Getter @Builder
    public static class MissionItem {
        private Long missionId;
        private String storeName;
        private String storeCategory;
        private String condition;
        private int rewardPoint;
        private float rewardRate;
        private Integer dDay;
        private String status;
        private LocalDateTime completedAt;
    }
    @Getter @Builder
    public static class SuccessResult {
        private Long missionId;
        private String status;
        private LocalDateTime requestedAt;
    }
}
