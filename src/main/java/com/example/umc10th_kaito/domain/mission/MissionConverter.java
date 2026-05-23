package com.example.umc10th_kaito.domain.mission;

import com.example.umc10th_kaito.domain.user.UserMission;
import org.springframework.data.domain.Page;

public class MissionConverter {

    public static MissionResDTO.MissionItem toMissionItem(UserMission userMission) {
        return MissionResDTO.MissionItem.builder()
                .missionId(userMission.getId())
                .storeName(userMission.getMission().getStore().getName())
                .storeCategory(userMission.getMission().getStore().getCategory())
                .condition(userMission.getMission().getContent())
                .rewardPoint(userMission.getMission().getRewardValue())
                .rewardType(userMission.getMission().getRewardType().name())
                .dDay(userMission.getMission().getDeadline())
                .status(userMission.getStatus().name())
                .completedAt(userMission.getStatus() == MissionStatus.COMPLETED
                        ? userMission.getUpdatedAt() : null)
                .build();
    }

    public static MissionResDTO.SuccessResult toSuccessResult(UserMission userMission) {
        return MissionResDTO.SuccessResult.builder()
                .missionId(userMission.getId())
                .status(userMission.getStatus().name())
                .requestedAt(userMission.getUpdatedAt())
                .build();
    }

    /**
     * JPA Page<T>를 커스텀 OffsetPageResponse<T>로 변환
     * Page의 raw 정보(pageable, sort 등)를 제거하고 필요한 것만 남긴다
     */
    public static <T> MissionResDTO.OffsetPageResponse<T> toOffsetPageResponse(Page<T> page) {
        return MissionResDTO.OffsetPageResponse.<T>builder()
                .data(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
