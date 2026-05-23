package com.example.umc10th_kaito.domain.mission.dto;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

public class MissionResDTO {
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
        private LocalDateTime completedAt;
    }
    @Getter @Builder
    public static class SuccessResult {
        private Long missionId;
        private String status;
        private LocalDateTime requestedAt;
    }

    @Getter @Builder
    public static class OffsetPageResponse<T> {
        private List<T> data;          // 실제 데이터 (content)
        private int pageNumber;        // 현재 페이지 번호
        private int pageSize;          // 페이지 크기
        private long totalElements;    // 전체 데이터 개수
        private int totalPages;        // 전체 페이지 수
        private boolean first;         // 첫 페이지인지
        private boolean last;          // 마지막 페이지인지
    }
}



