package com.example.umc10th_kaito.domain.mission.converter;
import com.example.umc10th_kaito.domain.mission.dto.MissionResDTO;
import com.example.umc10th_kaito.domain.user.entity.mapping.UserMission;
import com.example.umc10th_kaito.domain.user.enums.MissionStatus;
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
}
