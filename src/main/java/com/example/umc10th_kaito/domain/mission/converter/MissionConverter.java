package com.example.umc10th_kaito.domain.mission.converter;
import com.example.umc10th_kaito.domain.mission.dto.MissionResDTO;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
import org.springframework.data.domain.Page;

public class MissionConverter {
    public static MissionResDTO.MissionItem toMissionItem(UserMission userMission) {
        return MissionResDTO.MissionItem.builder()
                .missionId(userMission.getId())
                .storeName(userMission.getMission().getStore().getName())
                .storeCategory(userMission.getMission().getStore().getCategory())
                .condition(userMission.getMission().getContent())
                .rewardPoint(userMission.getMission().getRewardValue())
                .rewardType(userMission.getMission().getRewardType().name())  // 추가
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
