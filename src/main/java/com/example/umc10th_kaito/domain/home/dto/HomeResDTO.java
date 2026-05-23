package com.example.umc10th_kaito.domain.home.dto;
import lombok.Builder;
import lombok.Getter;
public class HomeResDTO {
    @Getter @Builder
    public static class Summary {
        private int completedCount;
        private int totalCount;
        private int rewardPoint;
        private int currentPoint;
        private String nickname;
        private String location;
    }
    @Getter @Builder
    public static class MissionItem {
        private Long missionId;
        private String storeName;
        private String storeCategory;
        private String condition;
        private int rewardPoint;
        private String rewardType; // "POINT" 또는 "RATE"
        private Integer dDay;
        private String status;
    }
}
