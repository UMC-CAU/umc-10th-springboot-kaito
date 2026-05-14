package com.example.umc10th_kaito.domain.mission;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class MissionResDTO {

    @Getter
    @Builder
    public static class MissionItem {
        private Long missionId;
        private String storeName;
        private String storeCategory;
        private String condition;
        private int rewardPoint;
        private String rewardType;
        private Integer dDay;
        private String status;
        private LocalDateTime completedAt;
    }

    @Getter
    @Builder
    public static class SuccessResult {
        private Long missionId;
        private String status;
        private LocalDateTime requestedAt;
    }

    /**
     * 오프셋 기반 페이지네이션 응답 DTO
     * Page<T>의 불필요한 정보를 걷어내고 필요한 것만 담는다
     */
    @Getter
    @Builder
    public static class OffsetPageResponse<T> {
        private List<T> data;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
    }
}
